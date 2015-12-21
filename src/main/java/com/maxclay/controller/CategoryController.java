package com.maxclay.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maxclay.model.Book;
import com.maxclay.model.Category;
import com.maxclay.model.User;
import com.maxclay.service.BookService;
import com.maxclay.service.CategoryService;

@Controller
public class CategoryController {
	
	private final BookService bookService;
	private final CategoryService categoryService;
		
	@Autowired
	public CategoryController(BookService bookService, CategoryService categoryService) {

		this.bookService = bookService;
		this.categoryService = categoryService;
	}
	
	@RequestMapping("/categories/category")
	public String showCategory(Model model, @RequestParam(required = true) String id, 
								@RequestParam(required = false, defaultValue = "1") Integer page) {
		
		List<Book> books = new ArrayList<Book>();
		Category category = categoryService.get(id);
		if(category.getBooks() != null)
			for(String bookId : category.getBooks())
				if(bookService.get(bookId) != null)
					books.add(bookService.get(bookId));
		
		long booksNum = books.size();
		long pagesNum = (booksNum % HomeController.BOOKS_ON_PAGE != 0) ? booksNum / HomeController.BOOKS_ON_PAGE + 1 
																		: booksNum / HomeController.BOOKS_ON_PAGE;
				
		books = (books.size() > page * HomeController.BOOKS_ON_PAGE) ? books.subList((page - 1) * HomeController.BOOKS_ON_PAGE, 
																							page * HomeController.BOOKS_ON_PAGE)
																		: books.subList((page - 1) * HomeController.BOOKS_ON_PAGE, books.size());

		
		model.addAttribute("categoryName", category.getName());
		model.addAttribute("books", books);
		model.addAttribute("pages_num", pagesNum);
		model.addAttribute("usersBooks", getUsersBooks());
		
		return "category";
	}
	
	@RequestMapping("/categories/add")
	public String addCategory(@RequestParam(required = true) String name, RedirectAttributes redirectAttrs) {
		
		if(categoryService.getByName(name.trim()) != null)
			redirectAttrs.addFlashAttribute("error", "Category alredy exists");
		else
			categoryService.add(new Category(name.trim(), null));
		
		return "redirect:/management/category";
	}
	
	@ResponseBody
	@RequestMapping("/categories/all")
	public List<Category> getAllCategories() {
		
		return categoryService.getAll();
	}
	
	private List<String> getUsersBooks() {
		 
		List<String> list = new ArrayList<String>();
		User user = ProfileController.getAuthenticatedUser();
		if(user != null && user.getBooks() != null)
			list = user.getBooks();
		
		return list; 
	}

}
