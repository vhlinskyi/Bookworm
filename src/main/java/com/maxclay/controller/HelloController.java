package com.maxclay.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maxclay.dao.BookDao;
import com.maxclay.model.Book;


@Controller
public class HelloController {
	
	@Autowired
	BookDao bookDao;
	
	@RequestMapping("/")
	public String home(Model model) {
		
		model.addAttribute("books", bookDao.getAll());
		return "homePage";
	}
	
	@RequestMapping("/add")
	public String add(@RequestParam(defaultValue = "default_name") String name,
						Model model) {
		
		Book book = new Book("c:\\" + name + ".pdf", name);
		bookDao.add(book);
		
		model.addAttribute("message", "Book '" + name + "' has been stored!");
		return "homePage";
	}
	
	 @RequestMapping(value = "/view", method = RequestMethod.GET)
	 public String preivewSection(@RequestParam(defaultValue = "default_name") String name,
			 						HttpServletRequest request, 
			 						HttpServletResponse response) {
	     try {
	    	 
	         byte[] documentInBytes = bookDao.getByName(name).getBookSourceInBytes();   
	         
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
