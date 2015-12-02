package com.maxclay.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.maxclay.validation.PasswordMatches;
import com.maxclay.validation.ValidEmail;

@PasswordMatches
public class UserDto {
	
	@NotNull
	@NotEmpty
	private String name;
	
	
	@NotNull
	@NotEmpty
	@ValidEmail
	private String email;
	
	@NotNull
	@NotEmpty
	private String password;
	
	@NotNull
	@NotEmpty
	private String matchingPassword;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}
	
	public String getMatchingPassword() {
		return matchingPassword;
	}
	
	@Override
    public String toString() {
        return String.format(
                "User DTO[\n name=%s,\n email_id='%s',\n password='%s', matchingPassword='%s']",
                name, email, password, matchingPassword);
    }

}
