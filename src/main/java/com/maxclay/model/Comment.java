package com.maxclay.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
	
	private String text;
	private String date;
	private String author;
	
	public Comment() {
		
		initDate();
	}
	
	public Comment(String text, String date, String author) {
		
		this.text = text;
		this.date = date;
		this.author = author;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void initDate() {
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();
		this.date = dateFormat.format(date);
	}
	
	@Override
	public String toString() {
		
		return String.format(
				 "Comment [\n text=%s,\n date='%s',\n author='%s']",
				 text, date, author);
	}

}
