package com.maxclay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "upload.userpic")
public class UserPicturesUploadProperties {
	
	private Resource uploadPath;
	private Resource defaultUserPicture;
	
	public Resource getDefaultUserPicture() {
		
		return defaultUserPicture;
	}
	
	public void setDefaultUserPicture(String defaultUserPicture) {
		
		this.defaultUserPicture = new DefaultResourceLoader().getResource(defaultUserPicture);
	}
	
	public Resource getUploadPath() {
		
		return uploadPath;
	}
	
	public void setUploadPath(String uploadPath) {
		
		this.uploadPath = new DefaultResourceLoader().getResource(uploadPath);
	}
}
