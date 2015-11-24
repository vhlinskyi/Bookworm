package com.maxclay.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maxclay.config.BookPicturesUploadProperties;
import com.maxclay.dao.BookDao;
import com.maxclay.dao.BookSourceDao;
import com.maxclay.model.Book;
import com.maxclay.model.BookSource;

@Controller
public class BookController {
	
	private final Resource picturesDir;
	private final Resource defaultBookPicture;
	
	private final BookSourceDao bookSourceDao;
	private final BookDao bookDao;
	
	@Autowired
	public BookController(BookSourceDao bookSourceDao, BookDao bookDao, 
			BookPicturesUploadProperties uploadProperties) {
		
		this.bookSourceDao = bookSourceDao;
		this.bookDao = bookDao;
		picturesDir = uploadProperties.getUploadPath();
		defaultBookPicture = uploadProperties.getDefaultBookPicture();
	}
	
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
    public String addBook(@ModelAttribute("book") Book book, HttpServletRequest request, 
    		HttpServletResponse response, RedirectAttributes redirectAttrs) throws IOException {
    	
    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    	
       	MultipartFile bookSourceFile = multipartRequest.getFile("file");
    	MultipartFile bookImageFile = multipartRequest.getFile("image");
    	
    	if (bookSourceFile.isEmpty() || !isPdf(bookSourceFile)) {
    		
    		redirectAttrs.addFlashAttribute("error", "Incorrect file. Please upload a PDF file.");
    		return "redirect:/add";
    	}
    	//TODO change it? capability to upload only pdf file?
    	if (bookImageFile.isEmpty() || !isImage(bookImageFile)) {
    		
    		redirectAttrs.addFlashAttribute("error", "Incorrect file. Please upload a picture.");
    		return "redirect:/add";
    	}

    	setSource(book, bookSourceFile);
    	setImage(book, bookImageFile);
    	bookDao.add(book);
    	
        return "redirect:/";
    }
	
	 @RequestMapping(value = "/view", method = RequestMethod.GET)
	 public void readBook(@RequestParam(required = true) String source,
			 HttpServletRequest request, HttpServletResponse response) throws IOException {
		 
	     try(OutputStream out = response.getOutputStream()) {
	    	 
	         byte[] documentInBytes = bookSourceDao.get(source).getBookSourceInBytes();   
	         response.setDateHeader("Expires", -1);
	         response.setContentType("application/pdf");
	         response.setContentLength(documentInBytes.length);
	         out.write(documentInBytes);
	     }
	 }
	 
	 @RequestMapping(value = "/uploadedPicture")
	 public void getUploadedPicture(@RequestParam(required = true) String id, HttpServletResponse response) throws IOException {
		 
		 Resource pic;
		 pic = new FileSystemResource(bookDao.get(id).getPath());
		 
		 if(!pic.exists())
			 pic = defaultBookPicture;
		 
		 response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(pic.getFilename()));
		 IOUtils.copy(pic.getInputStream(), response.getOutputStream());	


	 }

	 private void setSource(Book book, MultipartFile bookSourceFile) throws IOException {
	    	
		 BookSource bookSource = new BookSource();
		 bookSource.setFileName(bookSourceFile.getOriginalFilename());
		 bookSource.setBookSourceInBytes(IOUtils.toByteArray(bookSourceFile.getInputStream()));
		 bookSource = bookSourceDao.add(bookSource);
		 book.setSource(bookSource.getId());
	 }
	 
	 private void setImage(Book book, MultipartFile imageFile) throws IOException {
		 
		 String fileExtension = getFileExtension(imageFile.getOriginalFilename());
		 File tempFile = File.createTempFile("pic", fileExtension, picturesDir.getFile());
		 
		 try (InputStream in = imageFile.getInputStream(); OutputStream out = new FileOutputStream(tempFile)) {
				 IOUtils.copy(in, out);
				 book.setPath(new FileSystemResource(tempFile).getPath());
		
		 }
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
