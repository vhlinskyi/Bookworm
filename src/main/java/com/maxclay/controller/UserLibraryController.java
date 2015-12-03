package com.maxclay.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maxclay.model.Book;
import com.maxclay.model.User;
import com.maxclay.service.BookService;
import com.maxclay.service.UserService;

@Controller
public class UserLibraryController {

	private final BookService bookService;
	private final UserService userService;
	
	@Autowired
	public UserLibraryController(BookService bookService, UserService userService) {
		
		this.bookService = bookService;
		this.userService = userService;
		
	}
	
	@RequestMapping("/library")
	public String library(Model model) {
		
		List<Book> books = new ArrayList<Book>();
		
		System.out.println(ProfileController.authenticatedUser());
		User user = userService.get(ProfileController.authenticatedUser().getId());
		System.out.println(user);
		
		for(String bookId  : user.getBooks())
			books.add(bookService.get(bookId));
		
		model.addAttribute("books", books);
		return "user_library";
	}
	
	@RequestMapping(value = "/library/add", method = RequestMethod.GET)
	public String addBookToLibrary(@RequestParam(required = true) String id) {
		
		//TODO re login user and retrieve data from UserPrincipal instance?
		User user = userService.get(ProfileController.authenticatedUser().getId());
		
		user.addBook(id);
		
		userService.save(user);
		
		return "redirect:/";
	}
	
}
