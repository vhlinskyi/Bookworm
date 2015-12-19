package com.maxclay.dto;

import com.maxclay.model.Book;

public class BookDto {
	
	private String id;
	private String title;
	private String author;
	private short year;
	private short pages;
	private String language;
	private String description;
	private String category;
	
	public BookDto() {
		
	}
	
	public BookDto(Book book) {
		
		setId(book.getId());
		setTitle(book.getTitle());
		setAuthor(book.getAuthor());
		setYear(book.getYear());
		setPages(book.getPages());
		setLanguage(book.getBookLanguage());
		setDescription(book.getDescription());
		setCategory(book.getCategory());
	}
	
	public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }
    
    public short getPages() {
        return pages;
    }

    public void setPages(short pages) {
        this.pages = pages;
    }
    
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    @Override
    public String toString() {
        return String.format(
                "Book DTO [\n id=%s,\n title='%s',\n author='%s',\n year='%d',"
                + "\n pages='%d',\n language='%s,\n description='%s', category='%s']",
                id, title, author, year, pages, language, description, category);
    }

}
