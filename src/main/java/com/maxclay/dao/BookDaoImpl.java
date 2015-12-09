package com.maxclay.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import com.maxclay.model.Book;

@Repository
public class BookDaoImpl implements BookDao {

	 
	private final MongoOperations mongoOperations;
	
	@Autowired
	public BookDaoImpl(MongoOperations mongoOperations) {
		
		this.mongoOperations = mongoOperations;
		
	}
	
	@Override
	public void add(Book book) {
		
		mongoOperations.save(book);
	}

	@Override
	public Book get(String id) {
		
		return mongoOperations.findOne(Query.query(Criteria.where("id").is(id)), Book.class);
	}

	@Override
	public List<Book> getAll() {
		
		return mongoOperations.findAll(Book.class);
	}

	@Override
	public List<Book> getByTitle(String title) {
		
		return mongoOperations.find(Query.query(Criteria.where("title").is(title)), Book.class);
	}

	@Override
	public List<Book> getByAuthor(String author) {
		
		return mongoOperations.find(Query.query(Criteria.where("author").is(author)), Book.class);
	}

	@Override
	public List<Book> getByYear(short year) {
		
		return mongoOperations.find(Query.query(Criteria.where("year").is(year)), Book.class);
	}

	@Override
	public void delete(Book book) {
		
		mongoOperations.remove(book);
	}

	@Override
	public void delete(String id) {
		
		mongoOperations.remove(Query.query(Criteria.where("id").is(id)), Book.class);
	}

	@Override
	public List<Book> find(String... words) {
		
		TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(words);
		Query query = TextQuery.queryText(criteria).sortByScore();
		List<Book> books = mongoOperations.find(query, Book.class);
		
		return books;
	}

}
