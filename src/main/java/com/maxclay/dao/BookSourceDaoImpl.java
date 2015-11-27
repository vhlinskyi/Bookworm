package com.maxclay.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import com.maxclay.model.Book;
import com.maxclay.model.BookSource;
import com.mongodb.gridfs.GridFSDBFile;

@Repository
public class BookSourceDaoImpl implements BookSourceDao{

	 
	private final GridFsTemplate gridFsTemplate;

	@Autowired
	public BookSourceDaoImpl(GridFsTemplate gridFsTemplate) {
		
		this.gridFsTemplate = gridFsTemplate;
		
	}
	
	@Override
	public BookSource add(BookSource book) {
		
		gridFsTemplate.store(new ByteArrayInputStream(book.getBookSourceInBytes()), book.getFileName());
		
		return getByFileName(book.getFileName());
	}

	@Override
	public BookSource getByFileName(String name) {
		
		GridFSDBFile file = gridFsTemplate.findOne(Query.query(GridFsCriteria.whereFilename().is(name)));
		return gridFileToBookSource(file);
	}

	@Override
	public void delete(String id) {
		
		gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));
		
	}

	@Override
	public BookSource get(String id) {
		
		GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
		return gridFileToBookSource(file);
	}

	@Override
	public BookSource getByBook(Book book) {
		
		GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(book.getSource())));
		return gridFileToBookSource(file);
	}

	@Override
	public void delete(BookSource bookSource) {
		if(bookSource.getId() != null && !bookSource.getId().equals(""))
			delete(bookSource.getId());
	}

	@Override
	public void delete(Book book) {
		if(book.getSource() != null && !book.getSource().equals(""))
			delete(book.getSource());
	}
	
	private BookSource gridFileToBookSource(GridFSDBFile file) {
		
		BookSource bookSource = new BookSource();
		try {
			bookSource.setBookSourceInBytes(IOUtils.toByteArray(file.getInputStream()));
		} catch (IOException e) {
			System.out.println("Converting GridFSDBFile to BookSource error!");
			e.printStackTrace();
		}
		bookSource.setFileName(file.getFilename());
		bookSource.setId(file.get("_id").toString());
		
		return bookSource;
	}
	
}
