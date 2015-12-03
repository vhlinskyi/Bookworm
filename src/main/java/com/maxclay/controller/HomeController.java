package com.maxclay.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maxclay.dao.BookSourceDao;
import com.maxclay.model.Book;
import com.maxclay.model.User;
import com.maxclay.service.BookService;
import com.maxclay.service.UserService;

@Controller
public class HomeController {
	
	private final BookSourceDao bookSourceDao;
	private final BookService bookService;
	private final UserService userService;
		
	@Autowired
	public HomeController(BookSourceDao bookSourceDao, BookService bookService, UserService userService) {
		
		this.bookSourceDao = bookSourceDao;
		this.bookService = bookService;
		this.userService = userService;
	}
	
	@RequestMapping("/")
	public String home(Model model) {
		
		//TODO re login user and retrieve data from UserPrincipal instance????
		//TODO change it
		List<String> list = new ArrayList<String>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(!(auth instanceof AnonymousAuthenticationToken)) {
			
			User user = userService.get(ProfileController.authenticatedUser().getId());			
			
			if(user.getBooks() != null)
				list = user.getBooks();
		}
		
		model.addAttribute("books", bookService.getAll());
		model.addAttribute("list", list);
		return "index";
	}
	
	@RequestMapping("/book")
	public String showBook(@RequestParam(required = true) String id, Model model) {
        
		Book book = bookService.get(id);
		model.addAttribute("book", book);
		
		return "show_book";
	}
	
	 @RequestMapping(value = "/view", method = RequestMethod.GET)
	 public void readBook(@RequestParam(required = true) String source,
			 HttpServletRequest request, HttpServletResponse response) throws IOException {
		 
	     try(OutputStream out = response.getOutputStream()) {
	    	 
	         byte[] documentInBytes = bookSourceDao.get(source).getBookSourceInBytes();   
	         response.setDateHeader("Expires", -1);
	         response.setContentType("application/pdf");
	         response.setContentLength(documentInBytes.length);
	         out.write(documentInBytes);
	     }
	 }
	 
}
