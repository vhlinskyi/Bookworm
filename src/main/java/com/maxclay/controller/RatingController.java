package com.maxclay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maxclay.model.Book;
import com.maxclay.model.User;
import com.maxclay.service.BookService;

@Controller
public class RatingController {
	
	private final BookService bookService;
	
	@Autowired
	public RatingController(BookService bookService) {
		this.bookService = bookService;
	}
	
	@RequestMapping(value = "/book/rating", method = RequestMethod.GET)
	@ResponseBody
	public String showGuestList(@RequestParam(required = true) String score, 
			@RequestParam(required = true) String bookId) {
	    		
		User user = ProfileController.getAuthenticatedUser();
		Book bookForRate = bookService.get(bookId);
		
		bookForRate.rate(user.getId(), Integer.valueOf(score));
		bookService.add(bookForRate);

		return null;
	}

}
