package com.maxclay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.maxclay.model.User;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserService userService;
	
	@Autowired
	public CustomUserDetailsService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userService.getByEmail(email);
		if(user == null)
			throw new UsernameNotFoundException("User with email: " + email + " does not exist");
		
		return new UserPrincipal(user);
	}

}
