package com.maxclay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maxclay.dao.BookDao;
import com.maxclay.dto.BookDto;
import com.maxclay.model.Book;

@Service
public class BookServiceImpl implements BookService {

	private final BookDao bookDao;
	
	@Autowired
	public BookServiceImpl(BookDao bookDao) {
		
		this.bookDao = bookDao;
	}
	
	@Override
	public void add(Book book) {
		
		bookDao.add(book);
	}
	
	@Override
	public Book add(BookDto bookDto) {
		
		Book book = new Book();
		updateBookFields(book, bookDto);
		
		return update(book);
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
		
		bookDao.delete(book);		
	}

	@Override
	public void delete(String id) {
		bookDao.delete(id);		
	}

	@Override
    public Book update(BookDto bookDto) {
    	
		Book book = bookDao.get(bookDto.getId());
		updateBookFields(book, bookDto);
    	return update(book);
    }

	@Override
	public Book update(Book book) {
		
		bookDao.add(book);
		return get(book.getId());
	}
	
	private void updateBookFields(Book book, BookDto bookDto) {
		
		book.setAuthor(bookDto.getAuthor());
    	book.setDescription(bookDto.getDescription());
    	book.setLanguage(bookDto.getLanguage());
    	book.setPages(bookDto.getPages());
    	book.setTitle(bookDto.getTitle());
    	book.setYear(bookDto.getYear());
	}
	
}
