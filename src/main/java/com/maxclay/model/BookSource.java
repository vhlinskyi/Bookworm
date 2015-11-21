package com.maxclay.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.data.annotation.Id;

public class BookSource {
	
	@Id
	private String id;
	
	private byte[] bookSourceInBytes;
	private String fileName;
	
	public BookSource() {}
	
	public BookSource(String fileName, String filePath, Long bookID) {
		
		
		try {
			InputStream inputStream = new FileInputStream(new File(filePath));
			bookSourceInBytes =  IOUtils.toByteArray(inputStream);
			this.fileName = fileName;
		} catch (IOException e) {
			System.out.println("Book creating error! Source not found!");
			e.printStackTrace();
		}
	}
	
	public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
	public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
	
	public void setBookSourceInBytes(byte[] bookSourceInBytes) {
		this.bookSourceInBytes = bookSourceInBytes;
	}
	
	public byte[] getBookSourceInBytes() {
		return bookSourceInBytes;
	}
	
}
