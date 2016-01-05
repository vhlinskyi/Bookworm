package com.maxclay.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maxclay.dao.BookSourceDao;
import com.maxclay.model.Book;
import com.maxclay.model.User;
import com.maxclay.model.UsersBooksCount;
import com.maxclay.service.BookService;
import com.maxclay.service.CategoryService;
import com.maxclay.service.UserService;


@Controller
public class ManagementController {
	
	private final CategoryService categoryService;
	private final BookService bookService;
	private final UserService userService;
	
	private final BookSourceDao bookSourceDao;
	
	@Autowired
	public ManagementController(CategoryService categoryService, BookService bookService, 
			UserService userService, BookSourceDao bookSourceDao) {
		
		this.categoryService = categoryService;
		this.bookService = bookService;
		this.userService = userService;
		this.bookSourceDao = bookSourceDao;
	}
	
	@RequestMapping("/management/category")
	public String managementCategory(Model model) {
		
		model.addAttribute("categories", categoryService.getAll());
		return "management_category";
	}
	
	@RequestMapping("/management/statistics")
	public String managementStatistics(Model model) {
		
		model.addAttribute("ratedBooks", getRatedBooks());		
		model.addAttribute("commentedBooks", getCommentedBooks());	
		model.addAttribute("favoritesBooks", getFavoritesBooks());		
		return "management_statistics";
	}
	
	@RequestMapping("/management/books")
	public String managementBooks(Model model) {
		
		model.addAttribute("books", bookService.getAll());
		return "management_books";
	}
	
	@ResponseBody
	@RequestMapping(value = "/management/books/delete", method = RequestMethod.POST)
	public String deleteBook(@RequestBody final List<String> books) throws IOException {
	     
		for(String b : books) {
			Book book = bookService.get(b);
			  
	    	if(book != null && book.getPath() != null && !book.getPath().equals(""))
	    		deleteBookPicture(book);

	    	bookSourceDao.delete(book);
	    	bookService.delete(book);
		}
	    
	    return null;
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
	
	@RequestMapping("/management/users")
	public String managementUsers(Model model) {
		
		List<User> users = userService.getAll();
		User currentUser = ProfileController.getAuthenticatedUser();
		setCurrentUserToFront(users, currentUser);
		
		model.addAttribute("users", users);
		model.addAttribute("currentUserId", currentUser.getId());
		return "management_users";
	}
	
	@ResponseBody
	@RequestMapping(value = "/management/users/delete", method = RequestMethod.POST)
	public String deleteUsers(@RequestBody final List<String> users) throws IOException {
	    
		for(String userId : users) {
			
			deleteUserPicture(userService.get(userId));
			userService.delete(userId);
		}
			
	    return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/management/users/ban", method = RequestMethod.POST)
	public String banUsers(@RequestBody final List<String> users) {
	     
		setUsersEnabled(users, false);
	    return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/management/users/unban", method = RequestMethod.POST)
	public String unbanUsers(@RequestBody final List<String> users) {
	     
		setUsersEnabled(users, true);
	    return null;
	}
	
	private void deleteUserPicture(User user) throws IOException {
		if(user.getPicture() == null || user.getPicture().equals(""))
			return;
		
		Path path = Paths.get(user.getPicture());
		Files.delete(path);
	 }
	
	private void setUsersEnabled(List<String> users, boolean enabled) {
		
		for(String userId : users) {
			
			User user = userService.get(userId);
			user.setEnabled(enabled);
			userService.save(user);
		}
	}
	
	private void setCurrentUserToFront(List<User> users, User currentUser) {
		
		int index = 0;
		for(int i = 0; i < users.size(); i++)
			if(users.get(i).getId().equals(currentUser.getId()))
				index = i;
	
		Collections.swap(users, 0, index);
	}
	
	private List<Book> getRatedBooks() {
		
		List<Book> books = bookService.getAll();
		if(books == null)
			return null;
		
		Collections.sort(books);
		if(books.size() > 10)
			books = books.subList(0, 10);
		
		return books;
	}
	
	private List<Book> getCommentedBooks() {
		
		if(bookService.getAll() == null)
			return null;
		
		List<Book> commentedBooks = new ArrayList<Book>(bookService.getAll());
		commentedBooks.sort(
				(b1, b2) -> 
				{
					int c1 = b1.getComments() == null ? 0 : b1.getComments().size(); 
					int c2 = b2.getComments() == null ? 0 : b2.getComments().size();
					
					return c2 - c1;
				});
		
		if(commentedBooks.size() > 10)
			commentedBooks = commentedBooks.subList(0, 10);
		
		return commentedBooks;
	}
	
	private List<UsersBooksCount> getFavoritesBooks() {
		
		List<UsersBooksCount> favoritesBooks = userService.getFavoritesBooks();
		if(userService.getFavoritesBooks() != null && userService.getFavoritesBooks().size() > 10)
			favoritesBooks = favoritesBooks.subList(0, 10);
		
		for(UsersBooksCount ucb : favoritesBooks)
			ucb.setBook(bookService.get(ucb.getBookId()));
		
		return favoritesBooks;
	}
	
	private void deleteBookPicture(Book book) throws IOException {
		 Path path = Paths.get(book.getPath());
		 File f = path.toFile();
		 if(f.exists())
			 Files.delete(path);
	 }

}
