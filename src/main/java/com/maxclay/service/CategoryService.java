package com.maxclay.service;

import java.util.List;

import com.maxclay.model.Category;

public interface CategoryService {
	
	public void add(Category category);
	public void save(Category category);
	public Category get(String id);
	public Category getByName(String name);
	public List<Category> getAll();
	public void delete(Category category);
	public void delete(String id);
	
	public void move(List<String> booksIds, String categoryId);

}
