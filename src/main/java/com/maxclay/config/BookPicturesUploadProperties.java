package com.maxclay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "upload.bookpic")
public class BookPicturesUploadProperties {
	
	private Resource uploadPath;
	private Resource defaultBookPicture;
	
	public Resource getDefaultBookPicture() {
		
		return defaultBookPicture;
	}
	
	public void setDefaultBookPicture(String defaultBookPicture) {
		
		this.defaultBookPicture = new DefaultResourceLoader().getResource(defaultBookPicture);
	}
	
	public Resource getUploadPath() {
		
		return uploadPath;
	}
	
	public void setUploadPath(String uploadPath) {
		
		this.uploadPath = new DefaultResourceLoader().getResource(uploadPath);
	}
}
