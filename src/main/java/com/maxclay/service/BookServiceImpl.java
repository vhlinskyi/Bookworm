package com.maxclay.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maxclay.dao.BookDao;
import com.maxclay.dao.CategoryDao;
import com.maxclay.dto.BookDto;
import com.maxclay.model.Book;
import com.maxclay.model.Category;

@Service
public class BookServiceImpl implements BookService {

	private final BookDao bookDao;
	private final CategoryDao categoryDao; 
	
	@Autowired
	public BookServiceImpl(BookDao bookDao, CategoryDao categoryDao) {
		
		this.bookDao = bookDao;
		this.categoryDao = categoryDao;
	}
	
	@Override
	public void add(Book book) {
		
		bookDao.add(book);
	}
	
	@Override
	public Book add(BookDto bookDto) {
		
		Book book = new Book();
		updateBookFields(book, bookDto);
		
		return save(book);
	}

	@Override
	public Book get(String id) {

		return bookDao.get(id);
	}

	@Override
	public List<Book> getAll() {

		return bookDao.getAll();
	}

	@Override
	public List<Book> getByTitle(String title) {

		return bookDao.getByTitle(title);
	}

	@Override
	public List<Book> getByAuthor(String author) {

		return bookDao.getByAuthor(author);
	}

	@Override
	public List<Book> getByYear(short year) {

		return bookDao.getByYear(year);
	}

	@Override
	public void delete(Book book) {
		
		deleteBookFromCategories(book.getId());
		bookDao.delete(book);		
	}

	@Override
	public void delete(String id) {
		
		deleteBookFromCategories(id);
		bookDao.delete(id);		
	}

	@Override
    public Book save(BookDto bookDto) {
    	
		Book book = bookDao.get(bookDto.getId());
		updateBookFields(book, bookDto);
    	return save(book);
    }

	@Override
	public Book save(Book book) {
		
		bookDao.add(book);
		deleteBookFromCategories(book.getId());
		
		if(book.getCategory() != null && categoryDao.get(book.getCategory()) != null) {
			
			Category category = categoryDao.get(book.getCategory());			
			HashSet<String> booksSet = category.getBooks();
			if(booksSet == null)
				booksSet = new HashSet<String>();
				
			booksSet.add(book.getId());
			category.setBooks(booksSet);
			categoryDao.add(category);
		}
		
		bookDao.add(book);
		return get(book.getId());
	}
	
	@Override
	public List<Book> find(String... words) {

		return bookDao.find(words);
	}
	
	private void updateBookFields(Book book, BookDto bookDto) {
		
		book.setAuthor(bookDto.getAuthor());
    	book.setDescription(bookDto.getDescription());
    	book.setBookLanguage(bookDto.getLanguage());
    	book.setPages(bookDto.getPages());
    	book.setTitle(bookDto.getTitle());
    	book.setYear(bookDto.getYear());
    	
    	String categoryId = null;
    	if(!bookDto.getCategory().equals("default"))
    		categoryId = bookDto.getCategory();
    		
    	book.setCategory(categoryId);
	}
	
	private void deleteBookFromCategories(String bookId) {
		
		for(Category category : categoryDao.getAll()) {
			if(category.getBooks() != null && category.getBooks().contains(bookId)) {
				category.getBooks().remove(bookId);
				categoryDao.add(category);
			}
		}
	}
	
}
