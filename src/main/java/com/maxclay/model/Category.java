package com.maxclay.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@SuppressWarnings("serial")
@Document(collection = Category.COLLECTION_NAME)
public class Category implements Serializable {
	
	public static final String COLLECTION_NAME = "categories";
	
	@Id
	private String id;
	private String name;
	private List<String> books;
	
	public Category() {
		
	}
	
	public Category(String name, List<String> books) {
		
		this.name = name;
		this.books = books;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setBooks(List<String> books) {
		this.books = books;
	}
	
	public List<String> getBooks() {
		return books;
	}
	
	@Override
	public String toString() {
		return String.format("Category [name='%s']", name);
	}
}
