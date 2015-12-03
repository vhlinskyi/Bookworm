package com.maxclay.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@SuppressWarnings("serial")
@Document(collection = User.COLLECTION_NAME)
public class User implements Serializable {
	
	public static final String COLLECTION_NAME = "users";
	public static final String DEFAULT_USER_ROLE = "ROLE_USER";
	
	@Id
	private String id;
	private String name;
	private String email;
	private String password;
	private String picture;
	private String registrationDate;
	private String info;
	private List<String> roles;
	private boolean enabled;
	private List<String> books;
	
	 public User() {
		 
		 this.picture = "";
		 this.info = "";
		 
		 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		 Date date = new Date();
		 this.registrationDate = dateFormat.format(date);
	 }

	 public User(String name, String email, String password, String picture, List<String> books,
			 String registrationDate, String info, List<String> roles, boolean enabled) {
		 
		 this.name = name;
		 this.email = email;
		 this.password = password;
		 this.picture = picture;
		 this.registrationDate = registrationDate;
		 this.info = info;
		 this.roles = roles;
		 this.enabled = enabled;
		 this.books = books;
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
	 
	 public void setPicture(String picture) {
		 this.picture = picture;
	 }
	 
	 public String getPicture() {
		 return picture;
	 }
	 
	 public void setRegistrationDate(String registrationDate) {
		 this.registrationDate = registrationDate;
	 }
	 
	 public String getRegistrationDate() {
		 return registrationDate;
	 }
	 
	 public void setInfo(String info) {
		 this.info = info;
	 }
	 
	 public String getInfo() {
		 return info;
	 }
	 
	 public void setRoles(List<String> roles) {
		 this.roles = roles;
	 }
	 
	 public List<String> getRoles() {
		 return roles;
	 }
	 
	 public void setBooks(List<String> books) {
		 this.books = books;
	 }
	 
	 public List<String> getBooks() {
		 return books;
	 }
	 
	 public void setEnabled(boolean enabled) {
		 this.enabled = enabled;
	 }
	 
	 public boolean getEnabled() {
		 return enabled;
	 }
	 
	 public void addBook(String bookId) {
		 
		 if(books == null)
			 books = new ArrayList<String>();
		 
		 books.add(0, bookId);
	 }
	 
	 @Override
	 public String toString() {
		 return String.format(
				 "User [\n id=%s,\n name='%s',\n email='%s'"
				 + ",\n password='%s', \n picture='%s', \nregistration_date='%s', "
				 + "\ninfo='%s',\n first_role='%s'\n enabled='%b']",
				 id, name, email, password, picture, registrationDate, info, roles.get(0), enabled);
	    }

}
