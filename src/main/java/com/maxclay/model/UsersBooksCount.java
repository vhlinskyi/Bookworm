package com.maxclay.model;

/*
 * Represents how many users added certain book to the library.
 */
public class UsersBooksCount {
	
	private String bookId;
	private long count;
	private Book book;
	
	public UsersBooksCount() {
		
	}
	
	public UsersBooksCount(String bookId, long count, Book book) {
		this.bookId = bookId;
		this.count = count;
		this.book = book;
	}
	
	public void setBookID(String bookId) {
		this.bookId = bookId;
	}
	
	public String getBookId() {
		return bookId;
	}
	
	public void setCount(long count) {
		this.count = count;
	}
	
	public long getCount() {
		return count;
	}
	
	public void setBook(Book book) {
		this.book = book;
	}
	
	public Book getBook() {
		return book;
	}
	
	@Override
	public String toString() {
		return String.format("UsersBooksCount [bookId = '%s', count = '%s' ]", bookId, count);
	}
}
