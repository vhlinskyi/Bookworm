package com.maxclay.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	
	public static final short BOOKS_ON_PAGE = 5;
	
	private final BookSourceDao bookSourceDao;
	private final BookService bookService;
		
	@Autowired
	public HomeController(BookSourceDao bookSourceDao, BookService bookService) {
		
		this.bookSourceDao = bookSourceDao;
		this.bookService = bookService;
	}
	
	@RequestMapping("/")
	public String home(Model model) {
		
		model.addAttribute("books", bookService.getPopular(BOOKS_ON_PAGE));	
		model.addAttribute("pages_num", 0);
		model.addAttribute("usersBooks", getUsersBooks());
		
		return "index";
	}
	
	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public String allBooks(Model model, @RequestParam(required = false, defaultValue = "1") Integer page) {
		
		long booksNum = bookService.count();
		long pagesNum = (booksNum % BOOKS_ON_PAGE != 0) ? booksNum / BOOKS_ON_PAGE + 1 : booksNum / BOOKS_ON_PAGE;
				
		List<Book> books = bookService.get((page - 1) * BOOKS_ON_PAGE, page * BOOKS_ON_PAGE);
						
		model.addAttribute("books", books);
		model.addAttribute("pages_num", pagesNum);
		model.addAttribute("usersBooks", getUsersBooks());
		
		return "index";
	}
	
	@RequestMapping("/book")
	public String showBook(@RequestParam(required = true) String id, Model model) {
        
		Book book = bookService.get(id);
		model.addAttribute("book", book);
		
		if(book != null)
			model.addAttribute("initialRatingScore", getRatingScore(book));
		
		return "show_book";
	}
	
	@RequestMapping("/search")
	public String addCategory(@RequestParam(required = true) String searchParam, Model model) {
		
		List<Book> books = null;
		if(searchParam.length() > 0)		
			books = bookService.find(searchParam.split(" "));
		
		model.addAttribute("searchParam", searchParam);
		model.addAttribute("usersBooks", getUsersBooks());
		model.addAttribute("books", books);
		return "search_results";
	}
	
	@RequestMapping("/advanced_search")
	public String advancedSearch(Model model) {
		
		return "advanced_search";
	}
	
	//TODO validation
	@RequestMapping(value = "/advanced_search", method = RequestMethod.POST)
	public String advancedSearchResult(Model model, @RequestParam(required = true) String title, @RequestParam(required = true) String author,
										@RequestParam(required = true) String category, @RequestParam(required = false) String year,
										@RequestParam(required = true) String yearFrom, @RequestParam(required = true) String yearTo) {

		List<Book> books = bookService.getAll()
											   .stream()
											   .filter(book ->title.equals("") || book.getTitle().toLowerCase().contains(title.toLowerCase()))
											   .filter(book ->author.equals("") || book.getAuthor().toLowerCase().contains(author.toLowerCase()))
											   .filter(book ->category.equals("default") || (book.getCategory() != null && book.getCategory().equals(category)))
											   .filter(book ->year == null || year.equals("") || Short.valueOf(year).equals(book.getYear()))
											   .filter(book ->yearFrom.equals("default") || book.getYear() >= Short.valueOf(yearFrom))
											   .filter(book ->yearTo.equals("default") || book.getYear() <= Short.valueOf(yearTo))
											   .collect(Collectors.toList());
		
		model.addAttribute("usersBooks", getUsersBooks());
		model.addAttribute("books", books);
		
		return "search_results";
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public void readBook(@RequestParam(required = true) String source,
			 HttpServletRequest request, HttpServletResponse response) throws IOException {
		 
		response.setHeader("X-Frame-Options", "GOFORIT");
	    try(OutputStream out = response.getOutputStream()) {
	    	 
	    	byte[] documentInBytes = bookSourceDao.get(source).getBookSourceInBytes();   
	        response.setDateHeader("Expires", -1);
	        response.setContentType("application/pdf");
	        response.setContentLength(documentInBytes.length);
	        out.write(documentInBytes);
	        
	    }
	}
	
	@RequestMapping("/about")
	public String description() {

		return "about_project";
	}
	
	@RequestMapping("/help")
	public String help() {

		return "help";
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
