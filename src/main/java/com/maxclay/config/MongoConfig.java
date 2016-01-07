package com.maxclay.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
 


import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.ServerAddress;


import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
 
@Configuration
public class MongoConfig {
		
 
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
	 
	    if (System.getenv("OPENSHIFT_MONGODB_DB_HOST") != null) {
	 
	        String openshiftMongoDbHost = System.getenv("OPENSHIFT_MONGODB_DB_HOST");
	        int openshiftMongoDbPort = Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"));
	        String username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
	        String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
	        String databaseName = System.getenv("OPENSHIFT_APP_NAME");
	        
	        
	        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
	        seeds.add( new ServerAddress(openshiftMongoDbHost, openshiftMongoDbPort));
	        
	        
	        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
	        credentials.add(
	           MongoCredential.createCredential(username, databaseName, password.toCharArray())
	        );
	        
	        MongoClient mongoClient = new MongoClient( seeds, credentials );
	        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient, databaseName);
	        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
	       
	        return mongoTemplate;
	         
	    } else {
	 
	        return new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(), "test"));
	    }
	}
	
	@Bean
	public GridFsTemplate gridFSTemplate() throws Exception {
	 
	    if (System.getenv("OPENSHIFT_MONGODB_DB_HOST") != null) {
	 
	        String openshiftMongoDbHost = System.getenv("OPENSHIFT_MONGODB_DB_HOST");
	        int openshiftMongoDbPort = Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"));
	        String username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
	        String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
	        String databaseName = System.getenv("OPENSHIFT_APP_NAME");
	        
	        
	        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
	        seeds.add( new ServerAddress(openshiftMongoDbHost, openshiftMongoDbPort));
	        
	        
	        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
	        credentials.add(
	           MongoCredential.createCredential(username, databaseName, password.toCharArray())
	        );
	        
	        MongoClient mongoClient = new MongoClient( seeds, credentials );
	        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient, databaseName);
	        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
	       
	        MongoConverter mongoConverter = mongoTemplate.getConverter();
	        
	        GridFsTemplate gridFsTemplate = new GridFsTemplate(mongoDbFactory, mongoConverter);
	       
	        return gridFsTemplate;
	         
	    } else {
	 
	    	MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new MongoClient(), "test");
	    	MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
	        
	        MongoConverter mongoConverter = mongoTemplate.getConverter();
	        
	        GridFsTemplate gridFsTemplate = new GridFsTemplate(mongoDbFactory, mongoConverter);
	       
	        return gridFsTemplate;
	    }
	}
 
}