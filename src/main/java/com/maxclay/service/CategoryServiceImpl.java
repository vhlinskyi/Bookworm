package com.maxclay.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maxclay.dao.CategoryDao;
import com.maxclay.model.Book;
import com.maxclay.model.Category;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	private final CategoryDao categoryDao;
	private final BookService bookService;
	
	@Autowired
	public CategoryServiceImpl(CategoryDao categoryDao, BookService bookService) {
		this.categoryDao = categoryDao;
		this.bookService = bookService;
	}
	
	@Override
	public void add(Category category) {
		if(getByName(category.getName()) == null)
			categoryDao.add(category);
	}

	@Override
	public void save(Category category) {
		categoryDao.add(category);
	}

	@Override
	public Category get(String id) {
		return categoryDao.get(id);
	}

	@Override
	public Category getByName(String name) {
		return categoryDao.getByName(name);
	}

	@Override
	public List<Category> getAll() {
		return categoryDao.getAll();
	}

	@Override
	public void delete(Category category) {
		categoryDao.delete(category);		
	}

	@Override
	public void delete(String id) {
		categoryDao.delete(id);		
	}

	@Override
	public void move(List<String> booksIds, String categoryId) {
		
		for(String b : booksIds)
			excludeBook(b);
		
		if(!categoryId.equals("default"))
			addBooks(get(categoryId), booksIds);
		else 
			categoryId = null;
		
		for(String bId : booksIds) {
			
			Book book = bookService.get(bId);
			book.setCategory(categoryId);
			bookService.add(book);
		}
		
	}

	private void addBooks(Category category, List<String> booksIds) {
		
		if(category.getBooks() != null)
			category.getBooks().addAll(new HashSet<String>(booksIds));
		else
			category.setBooks(new HashSet<String>(booksIds));
		
		save(category);
	}
	
	private void excludeBook(String bookId) {
		
		for(Category category : getAll()) {
			if(category.getBooks() != null && category.getBooks().contains(bookId)) {
				category.getBooks().remove(bookId);
				save(category);
			}
		}
	}

}
