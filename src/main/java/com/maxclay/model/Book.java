package com.maxclay.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class Book {

	private String name;
	//TODO optimizing
	private byte[] bookSourceInBytes;
	
	public Book() {}
	
	public Book(String filePath, String name) {
		
		
		try {
			InputStream inputStream = new FileInputStream(new File(filePath));
			bookSourceInBytes =  IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			System.out.println("Book creating error! Source not found!");
			e.printStackTrace();
		}
		this.name = name;
	}
	
	public void setBookSourceInBytes(byte[] bookSourceInBytes) {
		this.bookSourceInBytes = bookSourceInBytes;
	}
	
	public byte[] getBookSourceInBytes() {
		return bookSourceInBytes;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
