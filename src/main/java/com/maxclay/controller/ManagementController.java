package com.maxclay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maxclay.service.BookService;
import com.maxclay.service.CategoryService;


@Controller
public class ManagementController {
	
	private final CategoryService categoryService;
	private final BookService bookService;
	
	@Autowired
	public ManagementController(CategoryService categoryService, BookService bookService) {
		
		this.categoryService = categoryService;
		this.bookService = bookService;
	}
	
	@RequestMapping("/management/category")
	public String managementCategory(Model model) {
		
		model.addAttribute("categories", categoryService.getAll());
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
			categoryService.delete(s);
	    
	    return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/management/category/addBooks", method = RequestMethod.POST)
	public String addBooks(@RequestBody final List<String> books, @RequestParam(required = true) String categoryId) {
	    
		categoryService.move(books, categoryId);
	    return null;
	}

}
