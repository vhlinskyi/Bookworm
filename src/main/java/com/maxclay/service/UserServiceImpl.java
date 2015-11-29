package com.maxclay.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maxclay.dao.UserDao;
import com.maxclay.model.User;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserDao userDao;
	
	@Autowired
	public UserServiceImpl(UserDao userDao) {
		
		this.userDao = userDao;
	}
	
	@Override
	public User register(UserDto profile) {
		
		//TODO - throw UserExistsException
		if(userDao.getByEmail(profile.getEmail()) != null)
			return null;
		
		User user = new User();
		user.setEmail(profile.getEmail());
		user.setEnabled(true);
		user.setName(profile.getName());
		//TODO password decoding
		user.setPassword(profile.getPassword());
		
		List<String> roles = new ArrayList<String>();
		//TODO???
		roles.add("ROLE_USER");
		user.setRoles(roles);
		
		userDao.add(user);
		
		return userDao.getByEmail(user.getEmail());
	}
	
	@Override
	public void save(User user) {
		
		userDao.add(user);
	}

	@Override
	public User get(String id) {

		return userDao.get(id);
	}

	@Override
	public User getByEmail(String email) {

		return userDao.getByEmail(email);
	}

	@Override
	public void delete(User user) {
		
		userDao.delete(user);
	}

	@Override
	public void delete(String id) {
		
		userDao.delete(id);
	}

}
