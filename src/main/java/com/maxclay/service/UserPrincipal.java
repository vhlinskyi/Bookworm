package com.maxclay.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.maxclay.model.User;

@SuppressWarnings("serial")
public class UserPrincipal extends User implements UserDetails {
	
	public UserPrincipal(User user) {
		super.setEmail(user.getEmail());
		super.setEnabled(user.getEnabled());
		super.setId(user.getId());
		super.setName(user.getName());
		super.setPassword(user.getPassword());
		super.setRoles(user.getRoles());
		super.setPicture(user.getPicture());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		String roles = "";
		 for(String role : getRoles())
			 roles += role + ",";
			
		 return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
	}

	@Override
	public String getUsername() {
		return getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return getEnabled();
	}

}
