package com.maxclay.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.maxclay.config.UserPicturesUploadProperties;
import com.maxclay.dto.UserDto;
import com.maxclay.model.User;
import com.maxclay.service.UserPrincipal;
import com.maxclay.service.UserService;

@Controller
public class ProfileController {
	
	private final UserService userService;
	
	private final Resource picturesDir;
	private final Resource defaultUserPicture;
	
	@Autowired
	public ProfileController(UserService userService, UserPicturesUploadProperties userPicturesUploadProperties) {
		
		this.userService = userService;
		this.picturesDir = userPicturesUploadProperties.getUploadPath();
		this.defaultUserPicture = userPicturesUploadProperties.getDefaultUserPicture();
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	 public String showRegistrationForm(Model model) {
		
		UserDto userDto = new UserDto();
		model.addAttribute("user", userDto);
		return "signup";
	 }
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String registerUserAccount(@ModelAttribute("user") @Valid UserDto accountDto, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
			return "/signup";
		
		User user = new User();
		user = registerUser(accountDto);
		
		if (user == null) {
			bindingResult.rejectValue("email", "message.regError");
			return "/signup";
		}
		
		login(user);
		userService.save(user);
		
		return "redirect:/profile";
	}
	
	 @RequestMapping("/profile")
	 public String showUserProfile() {
		 
		 return "user_profile"; 
	 }
	 
	 @RequestMapping(value = "/profile/edit", method = RequestMethod.GET)
	 public ModelAndView showEditUserProfileFrom() {
		 
		 return new ModelAndView("edit_profile", "user", authenticatedUser()); 
	 }
	 
	 @RequestMapping(value = "/profile/edit", method = RequestMethod.POST)
	 public ModelAndView editUser(@ModelAttribute("user") User user, MultipartFile file) throws IOException {
		 
		 if (!file.isEmpty() && !isImage(file)) {	    	
	    		
			 ModelAndView modelAndView = new ModelAndView("edit_profile");
			 modelAndView.addObject("user", user);
			 modelAndView.addObject("error", "Incorrect file. Please upload a pircture.");
			 return modelAndView;
		 }
	    	
		 setImage(user, file);
		 userService.save(user);
		 login(user);
		 	    	
		 return new ModelAndView("redirect:/profile/");
		 
	 }
	 
	 @RequestMapping(value = "/userPicture")
	 public void getUploadedPicture(HttpServletResponse response) throws IOException {
		 
		 Resource pic;
		 User user = authenticatedUser();
		 String path = user.getPicture();
		 
		 pic = (path != null) ? new FileSystemResource(path) : defaultUserPicture;
		 
		 if(!pic.exists())
			 pic = defaultUserPicture;
		 
		 response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(pic.getFilename()));
		 
		 OutputStream out = response.getOutputStream();
		 InputStream in = pic.getInputStream();
		 IOUtils.copy(in, out);

		 in.close();
		 out.close();

	 }
	 
	 private void setImage(User user, MultipartFile file) throws IOException {
		 
		 if(!file.isEmpty()) {
			 
			 if(user.getPicture() != null && !user.getPicture().equals(""))
				 deleteUserPicture(user);
			 
			 String fileExtension = getFileExtension(file.getOriginalFilename());
			 File tempFile = File.createTempFile("pic", fileExtension, picturesDir.getFile());
			 
			 try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(tempFile)) {
					 IOUtils.copy(in, out);
					 user.setPicture(new FileSystemResource(tempFile).getPath());
			 } 
		 }
	 }
	 
	 public static User authenticatedUser() {
		 
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 User user = (User) auth.getPrincipal();
		 return user;
	 }
	 
	 private void login(User user) {
		 
		 UserPrincipal userPrincipal = new UserPrincipal(user);
		 Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
		 SecurityContextHolder.getContext().setAuthentication(auth);
	 }
	
	 private void deleteUserPicture(User user) throws IOException {
		 Path path = Paths.get(user.getPicture());
		 Files.delete(path);
	 }
	 
	 private static String getFileExtension(String name) {
		 return name.substring(name.lastIndexOf("."));
	 }

	private boolean isImage(MultipartFile file) {
			
		 return file.getContentType().startsWith("image");
	 }
	
	 private User registerUser(UserDto accountDto) {
		
		 User registered = null;
		 registered = userService.register(accountDto);
		
		 if(registered == null)
			 return null;
	
		 return registered;
	 }
	
}
