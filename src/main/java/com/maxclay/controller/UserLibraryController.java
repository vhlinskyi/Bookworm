package com.maxclay.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maxclay.model.Book;
import com.maxclay.model.User;
import com.maxclay.service.BookService;
import com.maxclay.service.UserService;

@Controller
public class UserLibraryController {

	private final BookService bookService;
	private final UserService userService;
	
	@Autowired
	public UserLibraryController(BookService bookService, UserService userService) {
		
		this.bookService = bookService;
		this.userService = userService;
		
	}
	
	@RequestMapping("/library")
	public String library(Model model) {
		
		model.addAttribute("books", getUsersBooks());
		return "user_library";
	}
	
	@RequestMapping(value = "/library/add", method = RequestMethod.GET)
	public String addBookToLibrary(@RequestParam(required = true) String id) {
		
		User user = (User) ProfileController.getAuthenticatedUser();
		if(user.getBooks() == null || !user.getBooks().contains(id))
			user.addBook(id);
		
		userService.save(user);
		ProfileController.authenticateUser(user);
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/library/delete", method = RequestMethod.GET)
	public String deleteBookFromLibrary(@RequestParam(required = true) String id) {
		
		delete(id);		
		return "redirect:/library";
	}
	
	private void delete(String bookId) {
		
		User user = ProfileController.getAuthenticatedUser();
		user.getBooks().remove(bookId);
		
		userService.save(user);
		ProfileController.authenticateUser(user);
	}
	
	public List<Book> getUsersBooks() {
		
		List<Book> books = new ArrayList<Book>();
		User user = ProfileController.getAuthenticatedUser();
		List<String> usersBooks = user.getBooks();
		if(usersBooks == null)
			return books;
		int sizeBefore = usersBooks.size();
		for(Iterator<String> bookIter = usersBooks.iterator(); bookIter.hasNext();){
			String bookId = bookIter.next();
			if(bookService.get(bookId) != null && !bookService.get(bookId).equals(""))
				books.add(bookService.get(bookId));
			else
				bookIter.remove();
				
		}	

		if(sizeBefore != usersBooks.size())
			updateUsersBooks(books);
		
		return books;
	}

	private void updateUsersBooks(List<Book> books) {
		
		List<String> changedList = new ArrayList<String>();
		for(Book b : books)
			changedList.add(b.getId());
		
		User user = ProfileController.getAuthenticatedUser();
		user.setBooks(changedList);
		userService.save(user);
		ProfileController.authenticateUser(user);
	}
	
}
