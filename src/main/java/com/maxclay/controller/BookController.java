package com.maxclay.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.maxclay.dao.BookDao;
import com.maxclay.dao.BookSourceDao;
import com.maxclay.model.Book;

@Controller
public class BookController {
	
	@Autowired
	BookSourceDao bookSourceDao;
	
	@Autowired
	BookDao bookDao;
	
	@RequestMapping("/")
	public String home(Model model) {
		
		
		Book book = new Book();
		book.setTitle("" + System.currentTimeMillis());
		book.setSource("564d0f317d362ea0569caa0c");
		
		bookDao.add(book);
		
		model.addAttribute("books", bookDao.getAll());
		return "index";
	}
	
	@RequestMapping("/book")
	public String showBook(@RequestParam(defaultValue = "default_name") String name,
						HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        
		modelAndView.addObject("pdf",new String(Base64.encodeBase64(bookSourceDao.getByFileName(name).getBookSourceInBytes())));
		
		return "index";
	}
	
	
	@RequestMapping("/add")
	public String add(@RequestParam(defaultValue = "default_name") String name,
						Model model) {
		
		return "index";
	}
	
	 @RequestMapping(value = "/view", method = RequestMethod.GET)
	 public String preivewSection(@RequestParam(defaultValue = "default_name") String id,
			 						HttpServletRequest request, 
			 						HttpServletResponse response) {
	     try {
	    	 
	         byte[] documentInBytes = bookSourceDao.get(id).getBookSourceInBytes();   
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
