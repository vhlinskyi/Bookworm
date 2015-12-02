package com.maxclay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maxclay.model.Book;
import com.maxclay.model.Comment;
import com.maxclay.model.User;
import com.maxclay.service.BookService;

@Controller
public class CommentController {
	
	private final BookService bookService;
	
	@Autowired
	public CommentController(BookService bookService) {
		
		this.bookService = bookService;
	}
	
	@RequestMapping(value = "/book/comment", method = RequestMethod.POST)
	public @ResponseBody Object createComment(@RequestBody final Comment comment, @RequestParam(required = true) String bookId) {
	     
		comment.initDate();
		comment.setAuthor(currentUser().getName());
		
	    Book book = bookService.get(bookId);
	    book.addComment(comment);
	    bookService.add(book);
	    
	    return null;
	}
	 
	@RequestMapping(value = "/book/comment", method = RequestMethod.GET)
	public String showComments(Model model, @RequestParam(required = true) String bookId) {
		 
	    model.addAttribute("comments", bookService.get(bookId).getComments());
	     
	    return "comments :: commentsList";
	}
	
	private User currentUser() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) auth.getPrincipal();
		return user;	
	}

}
