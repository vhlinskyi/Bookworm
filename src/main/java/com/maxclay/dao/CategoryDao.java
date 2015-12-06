package com.maxclay.dao;

import java.util.List;

import com.maxclay.model.Category;

public interface CategoryDao {
	
	public void add(Category category);
	public Category get(String id);
	public Category getByName(String name);
	public List<Category> getAll();
	public void delete(Category category);
	public void delete(String id);
}
