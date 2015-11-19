package com.maxclay.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import com.maxclay.model.Book;
import com.mongodb.gridfs.GridFSDBFile;

@Repository
public class BookDaoImpl implements BookDao{

	 
	private final GridFsTemplate gridFsTemplate;

	@Autowired
	public BookDaoImpl(GridFsTemplate gridFsTemplate) {
		
		this.gridFsTemplate = gridFsTemplate;
		
	}
	
	@Override
	public void add(Book book) {
		
		gridFsTemplate.store(new ByteArrayInputStream(book.getBookSourceInBytes()), book.getName());
		
	}

	@Override
	public List<Book> getAll() {
		
		List<Book> books = new LinkedList<Book>();
		for(GridFSDBFile f : gridFsTemplate.find(null))
			books.add(gridFileToBook(f));
		
		return books;
	}

	@Override
	public Book getByName(String name) {
		
		GridFSDBFile file = gridFsTemplate.findOne(Query.query(GridFsCriteria.whereFilename().is(name)));
		
		return gridFileToBook(file);
	}

	@Override
	public void delete(String name) {
		
		gridFsTemplate.delete(Query.query(GridFsCriteria.whereFilename().is(name)));
		
	}
	
	private Book gridFileToBook(GridFSDBFile file) {
		
		Book book = new Book();
		try {
			book.setBookSourceInBytes(IOUtils.toByteArray(file.getInputStream()));
		} catch (IOException e) {
			System.out.println("Converting GridFSDBFile to Book error!");
			e.printStackTrace();
		}
		book.setName(file.getFilename());
		
		return book;
	}
	
}
