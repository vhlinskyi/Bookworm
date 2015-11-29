package com.maxclay.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@SuppressWarnings("serial")
@Document(collection = User.COLLECTION_NAME)
public class User implements Serializable {
	
	public static final String COLLECTION_NAME = "users";
	
	@Id
	private String id;
	private String name;
	private String email;
	private String password;
	private List<String> roles;
	private boolean enabled;
	
	 public User() {
	    }

	 public User(String name, String email, String password, List<String> roles, boolean enabled) {
		 
		 this.name = name;
		 this.email = email;
		 this.password = password;
		 this.roles = roles;
		 this.enabled = enabled;
	 }
	 
	 public void setId(String id) {
		 this.id = id;
	 }
	 
	 public String getId() {
		 return id;
	 }
	 
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
	 
	 public void setRoles(List<String> roles) {
		 this.roles = roles;
	 }
	 
	 public List<String> getRoles() {
		 return roles;
	 }
	 
	 public void setEnabled(boolean enabled) {
		 this.enabled = enabled;
	 }
	 
	 public boolean getEnabled() {
		 return enabled;
	 }
	 
	 @Override
	 public String toString() {
		 return String.format(
				 "User [\n id=%s,\n name='%s',\n email='%s'"
				 + ",\n password='%s',\n first_role='%s'\n enabled='%b']",
				 id, name, email, password, roles.get(0), enabled);
	    }

}
