package com.maxclay.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maxclay.dao.BookSourceDao;
import com.maxclay.model.Book;
import com.maxclay.model.User;
import com.maxclay.service.BookService;

@Controller
public class HomeController {
	
	private final BookSourceDao bookSourceDao;
	private final BookService bookService;
		
	@Autowired
	public HomeController(BookSourceDao bookSourceDao, BookService bookService) {
		
		this.bookSourceDao = bookSourceDao;
		this.bookService = bookService;
	}
	
	@RequestMapping("/")
	public String home(Model model) {
		
		List<Book> books = bookService.getAll();
		Collections.sort(books);
		model.addAttribute("books", books);
		model.addAttribute("usersBooks", getUsersBooks());
		
		return "index";
	}
	
	@RequestMapping("/book")
	public String showBook(@RequestParam(required = true) String id, Model model) {
        
		Book book = bookService.get(id);
		model.addAttribute("book", book);
		model.addAttribute("initialRatingScore", getRatingScore(book));
		
		return "show_book";
	}
	
	@RequestMapping("/search")
	public String addCategory(@RequestParam(required = true) String searchParam) {
		
		//TODO
		System.out.println("Search parameter: " + searchParam);
		
		return "search_results";
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
	 
	 private List<String> getUsersBooks() {
		 
		 List<String> list = new ArrayList<String>();
		 User user = ProfileController.getAuthenticatedUser();
		 if(user != null && user.getBooks() != null)
			 list = user.getBooks();
		
		 return list; 
	 }
	 
	 private int getRatingScore(Book book) {
		 User user = ProfileController.getAuthenticatedUser();
		 if(user == null || book.getRates() == null || !book.getRates().containsKey(user.getId()))
			 return 0;
		 else
			 return book.getRates().get(user.getId());
	 }

}
