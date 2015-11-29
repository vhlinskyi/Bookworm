package com.maxclay.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maxclay.model.User;
import com.maxclay.service.UserDto;
import com.maxclay.service.UserService;

@Controller
public class ProfileController {
	
	private final UserService userService;
	
	@Autowired
	public ProfileController(UserService userService) {
		
		this.userService = userService;
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
		
		Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.roles());
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		return "redirect:/";
	}
	
	private User registerUser(UserDto accountDto) {
		
		User registered = null;
		registered = userService.register(accountDto);
		
		if(registered == null)
			return null;
	
		return registered;
	}
	
}
