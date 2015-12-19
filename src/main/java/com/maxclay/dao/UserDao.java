package com.maxclay.dao;

import java.util.List;

import com.maxclay.model.UsersBooksCount;
import com.maxclay.model.User;

public interface UserDao {
	
	public void add(User user);
	
	public User get(String id);
	public User getByEmail(String email);
	public List<User> getAll();
	public List<User> getByName(String name);
	
	public void delete(User user);
	public void delete(String id);
	public List<UsersBooksCount> getFavoritesBooks();

}
