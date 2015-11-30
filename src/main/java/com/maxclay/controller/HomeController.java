package com.maxclay.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maxclay.config.BookPicturesUploadProperties;
import com.maxclay.dao.BookDao;
import com.maxclay.dao.BookSourceDao;
import com.maxclay.model.Book;

@Controller
public class HomeController {
	
	private final BookSourceDao bookSourceDao;
	private final BookDao bookDao;
	
	@Autowired
	public HomeController(BookSourceDao bookSourceDao, BookDao bookDao, 
			BookPicturesUploadProperties uploadProperties) {
		
		this.bookSourceDao = bookSourceDao;
		this.bookDao = bookDao;
	}
	
	@RequestMapping("/")
	public String home(Model model) {
		
		model.addAttribute("books", bookDao.getAll());
		return "index";
	}
	
	@RequestMapping("/book")
	public String showBook(@RequestParam(required = true) String id, Model model) {
        
		Book book = bookDao.get(id);
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
