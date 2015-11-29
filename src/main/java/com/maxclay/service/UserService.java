package com.maxclay.service;

import com.maxclay.model.User;

public interface UserService {
	
	public User register(UserDto profile);
	public void save(User user);
	public User get(String id);
	public User getByEmail(String email);
	public void delete(User user);
	public void delete(String id);
	
}
