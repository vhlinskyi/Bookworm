package com.maxclay.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maxclay.model.User;
import com.maxclay.service.BookService;
import com.maxclay.service.CategoryService;
import com.maxclay.service.UserService;


@Controller
public class ManagementController {
	
	private final CategoryService categoryService;
	private final BookService bookService;
	private final UserService userService;
	
	@Autowired
	public ManagementController(CategoryService categoryService, BookService bookService, UserService userService) {
		
		this.categoryService = categoryService;
		this.bookService = bookService;
		this.userService = userService;
	}
	
	@RequestMapping("/management/category")
	public String managementCategory(Model model) {
		
		model.addAttribute("categories", categoryService.getAll());
		return "management_category";
	}
	
	@RequestMapping("/management/books")
	public String managementBooks(Model model) {
		
		model.addAttribute("books", bookService.getAll());
		return "management_books";
	}
	
	@ResponseBody
	@RequestMapping(value = "/management/category/delete", method = RequestMethod.POST)
	public String deleteCategory(@RequestBody final List<String> categories) {
	     
		for(String s : categories)
			categoryService.delete(s);
	    
	    return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/management/category/addBooks", method = RequestMethod.POST)
	public String addBooks(@RequestBody final List<String> books, @RequestParam(required = true) String categoryId) {
	    
		categoryService.move(books, categoryId);
	    return null;
	}
	
	@RequestMapping("/management/users")
	public String managementUsers(Model model) {
		
		List<User> users = userService.getAll();
		User currentUser = ProfileController.getAuthenticatedUser();
		setCurrentUserToFront(users, currentUser);
		
		model.addAttribute("users", users);
		model.addAttribute("currentUserId", currentUser.getId());
		return "management_users";
	}
	
	@ResponseBody
	@RequestMapping(value = "/management/users/delete", method = RequestMethod.POST)
	public String deleteUsers(@RequestBody final List<String> users) {
	     
		for(String userId : users)
			userService.delete(userId);
			
	    return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/management/users/ban", method = RequestMethod.POST)
	public String banUsers(@RequestBody final List<String> users) {
	     
		setUsersEnabled(users, false);
	    return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/management/users/unban", method = RequestMethod.POST)
	public String unbanUsers(@RequestBody final List<String> users) {
	     
		setUsersEnabled(users, true);
	    return null;
	}
	
	private void setUsersEnabled(List<String> users, boolean enabled) {
		
		for(String userId : users) {
			
			User user = userService.get(userId);
			user.setEnabled(enabled);
			userService.save(user);
		}
	}
	
	private void setCurrentUserToFront(List<User> users, User currentUser) {
		
		int index = 0;
		for(int i = 0; i < users.size(); i++)
			if(users.get(i).getId().equals(currentUser.getId()))
				index = i;
	
		Collections.swap(users, 0, index);
	}

}
