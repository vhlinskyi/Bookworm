package com.maxclay.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maxclay.dao.UserDao;
import com.maxclay.dto.UserDto;
import com.maxclay.model.UsersBooksCount;
import com.maxclay.model.User;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
		
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;

	}
	
	@Override
	public User register(UserDto profile) {
		
		if(userDao.getByEmail(profile.getEmail()) != null)
			return null;
		
		User user = new User();
		user.setEmail(profile.getEmail());
		user.setEnabled(true);
		user.setName(profile.getName());

		String plainTextPassword = profile.getPassword();
		user.setPassword(passwordEncoder.encode(plainTextPassword));
		
		List<String> roles = new ArrayList<String>();

		roles.add(User.DEFAULT_USER_ROLE);
		user.setRoles(roles);
		
		if(userDao.getByEmail(user.getEmail()) == null)
			userDao.add(user);
		
		return userDao.getByEmail(user.getEmail());
	}
	
	@Override
	public User save(User user) {
		
		userDao.add(user);
		return get(user.getId());
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
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Override
	public List<User> getAll() {
		return userDao.getAll();
	}

	@Override
	public List<UsersBooksCount> getFavoritesBooks() {
		return userDao.getFavoritesBooks();
	}

}
