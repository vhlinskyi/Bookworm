package com.maxclay.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.maxclay.dao.BookDao;
import com.maxclay.dao.BookSourceDao;
import com.maxclay.model.Book;
import com.maxclay.model.BookSource;

@Controller
public class BookController {
	
	public static final Resource PICTURES_DIR = new FileSystemResource("./pictures");
	
	@Autowired
	BookSourceDao bookSourceDao;
	
	@Autowired
	BookDao bookDao;
	
	@RequestMapping("/")
	public String home(Model model) {
		
		model.addAttribute("books", bookDao.getAll());
		return "index";
	}
	
	@RequestMapping("/book")
	public String showBook(@RequestParam(required = true) String id, Model model) {
        
		Book book = bookDao.get(id);
		model.addAttribute("book", book);
		
		return "show_book";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView showAddForm() {

		return new ModelAndView("add_book", "book", new Book());
		
	}

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addBook(@ModelAttribute("book") Book book, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    	
       	MultipartFile bookSourceFile = multipartRequest.getFile("file");
    	MultipartFile bookImageFile = multipartRequest.getFile("image");
   
    	   
    	if(bookSourceFile != null)
       		setSource(book, bookSourceFile);

       	if(bookImageFile != null) {

       		String imageFilename = bookImageFile.getOriginalFilename();
       		File tempFile = File.createTempFile("pic", getFileExtension(imageFilename), PICTURES_DIR.getFile());
       		
       		try (InputStream in = bookImageFile.getInputStream(); OutputStream out = new FileOutputStream(tempFile)) {
       				
       			IOUtils.copy(in, out);
       			//TODO path setting
       			//String path 
       			//book.setPath();
       			
       		}
       	}

    	bookDao.add(book);
    	
        return "redirect:/";
    }
	
	 @RequestMapping(value = "/view", method = RequestMethod.GET)
	 public String readBook(@RequestParam(required = true) String source,
			 						HttpServletRequest request, 
			 						HttpServletResponse response) {
	     try {
	    	 
	         byte[] documentInBytes = bookSourceDao.get(source).getBookSourceInBytes();   
	         response.setDateHeader("Expires", -1);
	         response.setContentType("application/pdf");
	         response.setContentLength(documentInBytes.length);
	         response.getOutputStream().write(documentInBytes);
	     } catch (Exception e) {
	    	 e.printStackTrace();
	     }
	     return null;
	 }

	 private void setSource(Book book, MultipartFile bookSourceFile) throws IOException {
	    	
		 BookSource bookSource = new BookSource();
		 bookSource.setFileName(bookSourceFile.getOriginalFilename());
		 bookSource.setBookSourceInBytes(IOUtils.toByteArray(bookSourceFile.getInputStream()));
			
		 //bookSource.id == null
		 bookSourceDao.add(bookSource);
			
		 //retrieving bookSource id from mongoDB => bookSource.id != null
		 bookSource = bookSourceDao.getByFileName(bookSourceFile.getOriginalFilename());
		 book.setSource(bookSource.getId());
	 }
	 
	 private static String getFileExtension(String name) {
		 
		 return name.substring(name.lastIndexOf("."));
	 }
}
