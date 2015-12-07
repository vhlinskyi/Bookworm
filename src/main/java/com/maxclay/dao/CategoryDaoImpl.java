package com.maxclay.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.maxclay.model.Category;

@Repository
public class CategoryDaoImpl implements CategoryDao {
	
	private final MongoOperations mongoOperations;
	
	@Autowired
	public CategoryDaoImpl(MongoOperations mongoOperations) {
		
		this.mongoOperations = mongoOperations;	
	}

	@Override
	public void add(Category category) {
		mongoOperations.save(category);
	}

	@Override
	public Category get(String id) {

		return mongoOperations.findOne(Query.query(Criteria.where("id").is(id)), Category.class);
	}
	
	@Override
	public Category getByName(String name) {

		return mongoOperations.findOne(Query.query(Criteria.where("name").is(name)), Category.class);
	}

	@Override
	public List<Category> getAll() {

		return mongoOperations.findAll(Category.class);
	}

	@Override
	public void delete(Category category) {
		
		mongoOperations.remove(category);
	}

	@Override
	public void delete(String id) {
		
		mongoOperations.remove(Query.query(Criteria.where("id").is(id)), Category.class);
	}

}
