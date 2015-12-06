package com.maxclay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maxclay.dao.CategoryDao;
import com.maxclay.service.BookService;


@Controller
public class ManagementController {
	
	private final CategoryDao categoryDao;
	private final BookService bookService;
	
	@Autowired
	public ManagementController(CategoryDao categoryDao, BookService bookService) {
		
		this.categoryDao = categoryDao;
		this.bookService = bookService;
	}
	
	@RequestMapping("/management/category")
	public String managementCategory(Model model) {
		
		model.addAttribute("categories", categoryDao.getAll());
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
			categoryDao.delete(s);
	    
	    return null;
	}

}
