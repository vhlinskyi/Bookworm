package com.maxclay.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maxclay.config.BookPicturesUploadProperties;
import com.maxclay.dao.BookSourceDao;
import com.maxclay.dto.BookDto;
import com.maxclay.model.Book;
import com.maxclay.model.BookSource;
import com.maxclay.service.BookService;

@Controller
public class BookController {
	
	private final Resource picturesDir;
	private final Resource defaultBookPicture;
	
	private final BookSourceDao bookSourceDao;
	private final BookService bookService;
		
	@Autowired
	public BookController(BookSourceDao bookSourceDao, BookService bookService, 
			BookPicturesUploadProperties uploadProperties) {
		
		this.bookSourceDao = bookSourceDao;
		this.bookService = bookService;
		picturesDir = uploadProperties.getUploadPath();
		defaultBookPicture = uploadProperties.getDefaultBookPicture();
	}
	
	@RequestMapping(value = "/uploadedPicture")
	public void getUploadedPicture(@RequestParam(required = true) String id, HttpServletResponse response) throws IOException {
		 
		Resource pic;
		String path = bookService.get(id).getPath();
		 
		pic = (path != null) ? new FileSystemResource(path) : defaultBookPicture;
		
		if(!pic.exists())
			pic = defaultBookPicture;
		 
		response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(pic.getFilename()));
		 
		OutputStream out = response.getOutputStream();
		InputStream in = pic.getInputStream();
		IOUtils.copy(in, out);

		in.close();
		out.close();
		
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView showAddForm() {
		
		ModelAndView modelAndView = new ModelAndView("add_edit_book", "book", new BookDto());
		modelAndView.addObject("action", "add");
		return modelAndView;
	}

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addBook(@ModelAttribute("book") BookDto bookDto, HttpServletRequest request, 
    		HttpServletResponse response, RedirectAttributes redirectAttrs) throws IOException {

    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    	
       	MultipartFile bookSourceFile = multipartRequest.getFile("file");
    	MultipartFile bookImageFile = multipartRequest.getFile("image");
    	
    	if (bookSourceFile.isEmpty() || !isPdf(bookSourceFile)) {
    		
    		redirectAttrs.addFlashAttribute("error", "Incorrect file. Please upload a PDF file.");
    		return "redirect:/add";
    	}
    	
    	if (!bookImageFile.isEmpty() && !isImage(bookImageFile)) {
    		
    		redirectAttrs.addFlashAttribute("error", "Incorrect file. Please upload a picture.");
    		return "redirect:/add";
    	}
    	
    	Book book = bookService.add(bookDto);
    	setSource(book, bookSourceFile);
    	setImage(book, bookImageFile);
    	bookService.add(book);
    	
        return "redirect:/management/books";
    }
    
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView showBookEditForm(@RequestParam(required = true) String id) {
    	
    	BookDto bookDto = new BookDto(bookService.get(id));
    	ModelAndView modelAndView = new ModelAndView("add_edit_book", "book", bookDto);
		modelAndView.addObject("action", "edit");
		return modelAndView;
    }
    
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView editBook(@ModelAttribute("book") BookDto bookDto, HttpServletRequest request, 
    		HttpServletResponse response) throws IOException {
    	
    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    	
       	MultipartFile bookSourceFile = multipartRequest.getFile("file");
    	MultipartFile bookImageFile = multipartRequest.getFile("image");
    	
    	String error = "";
    	if (!bookSourceFile.isEmpty() && !isPdf(bookSourceFile))
    		error = "Incorrect file. Please upload a PDF file.";
    	
    	if (!bookImageFile.isEmpty() && !isImage(bookImageFile))
    		error = "Incorrect file. Please upload a pircture.";
    	
    	if(!error.equals("")) {
    		
    		ModelAndView modelAndView = new ModelAndView("add_edit_book");
    		modelAndView.addObject("book", bookDto);
    		modelAndView.addObject("action", "edit");
    		modelAndView.addObject("error", error);
    		return modelAndView;
    	}
    	
    	Book book = bookService.save(bookDto);
    	setSource(book, bookSourceFile);
    	setImage(book, bookImageFile);
    	
    	bookService.save(book);
    	
        return new ModelAndView("redirect:/books");
    }
    
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteBook(@RequestParam(required = true) String id) throws IOException {
    	
    	Book book = bookService.get(id);
  
    	if(book != null && book.getPath() != null && !book.getPath().equals(""))
    		deleteBookPicture(book);

    	bookSourceDao.delete(book);
    	bookService.delete(book);
    	
        return "redirect:/books";
    }
    
    private void setSource(Book book, MultipartFile bookSourceFile) throws IOException {
		 
		 if(!bookSourceFile.isEmpty()) {
			 
			 bookSourceDao.delete(book);
			 
			 BookSource bookSource = new BookSource();
			 bookSource.setFileName(bookSourceFile.getOriginalFilename().replace(".", System.currentTimeMillis() + "."));
			 bookSource.setBookSourceInBytes(IOUtils.toByteArray(bookSourceFile.getInputStream()));
			 bookSource = bookSourceDao.add(bookSource);
			 book.setSource(bookSource.getId());
		 }
	 }
	 
	 private void setImage(Book book, MultipartFile imageFile) throws IOException {
		 
		 if(!imageFile.isEmpty()) {
			 
			 if(book.getPath() != null && !book.getPath().equals(""))
		    		deleteBookPicture(book);
			 
			 String fileExtension = getFileExtension(imageFile.getOriginalFilename());
			 File tempFile = File.createTempFile("pic", fileExtension, picturesDir.getFile());
			 
			 try (InputStream in = imageFile.getInputStream(); OutputStream out = new FileOutputStream(tempFile)) {
					 IOUtils.copy(in, out);
					 book.setPath(new FileSystemResource(tempFile).getPath());
			 }
		 }
	 }	 
	 
	 private void deleteBookPicture(Book book) throws IOException {
		 Path path = Paths.get(book.getPath());
		 Files.delete(path);
	 }
	 
	 private static String getFileExtension(String name) {
		 return name.substring(name.lastIndexOf("."));
	 }
	 
	 private boolean isImage(MultipartFile file) {
		
		 return file.getContentType().startsWith("image");
	 }
	 
	 private boolean isPdf(MultipartFile file) {
	
		 return file.getContentType().equals("application/pdf");
	 }
	 
}
