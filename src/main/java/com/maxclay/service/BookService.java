package com.maxclay.service;

import java.util.List;

import com.maxclay.dto.BookDto;
import com.maxclay.model.Book;

public interface BookService {
	
	public void add(Book book);
	public Book add(BookDto bookDto);
	
	public Book get(String id);
	public List<Book> getAll();
	public List<Book> getByTitle(String title);
	public List<Book> getByAuthor(String author);
	public List<Book> getByYear(short year);
	
	public void delete(Book book);
	public void delete(String id);
	
	public Book update(BookDto bookDto);
	public Book update(Book book);
}
