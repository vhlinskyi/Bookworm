package com.maxclay.service;

import java.util.List;

import com.maxclay.dto.UserDto;
import com.maxclay.model.UsersBooksCount;
import com.maxclay.model.User;

public interface UserService {
	
	public User register(UserDto profile);
	public User save(User user);
	public User get(String id);
	public User getByEmail(String email);
	public List<User> getAll();
	public void delete(User user);
	public void delete(String id);
	public List<UsersBooksCount> getFavoritesBooks();
	
}
