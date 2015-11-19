package com.maxclay.dao;

import java.util.List;

import com.maxclay.model.Book;

public interface BookDao {

	public void add(Book book);
	public List<Book> getAll();
	public Book getByName(String name);
	public void delete(String name);
	
}
