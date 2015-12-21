package com.maxclay.dao;

import java.util.List;

import com.maxclay.model.Book;

public interface BookDao {
	
	public void add(Book book);
	
	public Book get(String id);
	public List<Book> getAll();
	public List<Book> getByTitle(String title);
	public List<Book> getByAuthor(String author);
	public List<Book> getByYear(short year);
	public List<Book> get(int fromIndex, int toIndex);
	
	public List<Book> find(String... words);
	public void delete(Book book);
	public void delete(String id);
	
	public long count();
	
}
