package com.maxclay.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maxclay.dao.BookDao;
import com.maxclay.dao.BookSourceDao;
import com.maxclay.model.Book;
import com.maxclay.model.BookSource;

@Controller
public class BookController {
	
	@Autowired
	BookSourceDao bookSourceDao;
	
	@Autowired
	BookDao bookDao;
	
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
	
	
	
	
	@RequestMapping("/add")
	public String add(@RequestParam(required = true) String filename) {
		//TODO change it
		BookSource bookSource = new BookSource();
		bookSource.setFileName(filename);
		
		Path path = Paths.get("c:/" + filename);
		
		try {
			bookSource.setBookSourceInBytes(Files.readAllBytes(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		bookSourceDao.add(bookSource);
		return "index";
	}
	
	 @RequestMapping(value = "/view", method = RequestMethod.GET)
	 public String readBook(@RequestParam(required = true) String source,
			 						HttpServletRequest request, 
			 						HttpServletResponse response) {
	     try {
	    	 
	         byte[] documentInBytes = bookSourceDao.get(source).getBookSourceInBytes();   
	         response.setDateHeader("Expires", -1);
	         response.setContentType("application/pdf");
	         response.setContentLength(documentInBytes.length);
	         response.getOutputStream().write(documentInBytes);
	     } catch (Exception e) {
	    	 e.printStackTrace();
	     }
	     return null;
	 }

	
}
