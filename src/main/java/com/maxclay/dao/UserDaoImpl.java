package com.maxclay.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.maxclay.model.UsersBooksCount;
import com.maxclay.model.User;

@Repository
public class UserDaoImpl implements UserDao {
	
	private final MongoOperations mongoOperations;
	
	@Autowired
	public UserDaoImpl(MongoOperations mongoOperations) {
		
		this.mongoOperations = mongoOperations;
		
	}

	@Override
	public void add(User user) {
		
		mongoOperations.save(user);
	}

	@Override
	public User get(String id) {
		
		return mongoOperations.findOne(Query.query(Criteria.where("id").is(id)), User.class);
	}

	@Override
	public User getByEmail(String email) {
		
		return mongoOperations.findOne(Query.query(Criteria.where("email").is(email)), User.class);
	}

	@Override
	public List<User> getAll() {

		return mongoOperations.findAll(User.class);
	}

	@Override
	public List<User> getByName(String name) {

		return mongoOperations.find(Query.query(Criteria.where("name").is(name)), User.class);
	}

	@Override
	public void delete(User user) {
		
		mongoOperations.remove(user);
	}

	@Override
	public void delete(String id) {
		
		mongoOperations.remove(Query.query(Criteria.where("id").is(id)), User.class);
	}

	@Override
	public List<UsersBooksCount> getFavoritesBooks() {
		
		Aggregation agg = newAggregation(
				match(Criteria.where("books").exists(true)),
				unwind("books"),
				group("books").count().as("count"),
				project("count").and("bookId").previousOperation(),
				sort(Sort.Direction.DESC, "count")
		);
		
		AggregationResults<UsersBooksCount> groupResults = mongoOperations.aggregate(agg, User.class, UsersBooksCount.class);
		List<UsersBooksCount> result = groupResults.getMappedResults();
		
		return result;
	}

}
