package com.maxclay.dao;

import com.maxclay.model.Book;
import com.maxclay.model.BookSource;

public interface BookSourceDao {

	public BookSource add(BookSource bookSource);
	public BookSource get(String id);
	public BookSource getByFileName(String fileName);
	public BookSource getByBook(Book book);
	public void delete(BookSource bookSource);
	public void delete(Book book);
	public void delete(String id);
	
}
