package com.maxclay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import com.maxclay.model.Book;
import com.maxclay.model.Category;
import com.maxclay.model.User;
import com.maxclay.service.BookService;
import com.maxclay.service.CategoryService;
import com.maxclay.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BookwormApplication.class)
@WebAppConfiguration
public class BookwormApplicationTests {
	
	@Autowired
    BookService bookService;
	
	@Autowired
    UserService userService;
	
	@Autowired
    CategoryService categoryService;

	
	Book testBook;
	User testUser;
	Category testCategory;

	@Before
    public void setUp() {
       
		setUpBook();
		setUpUser();
		setUpCategories();
    }
	
	private void setUpBook() {
		
		testBook = new Book();
		
		testBook.setTitle("testTitle");
		testBook.setAuthor("testAuthor");
		testBook.setBookLanguage("testLanguage");
		testBook.setPages(Short.valueOf("0"));
		bookService.add(testBook);

	}
	
	private void setUpUser() {
		
		testUser = new User();
		
		testUser.setEmail("test@gmail.com");
		testUser.setName("test");
		testUser.setInfo("test");
		
		userService.save(testUser);
	}
	
	private void setUpCategories() {
		
		testCategory = new Category("test", null);
		categoryService.add(testCategory);
	}
	
	@Test
	public void canFetchBookTest() {

		Book fetchedBook = bookService.get(testBook.getId());
		
		Assert.assertEquals(fetchedBook.getTitle(), "testTitle");
		Assert.assertEquals(fetchedBook.getAuthor(), "testAuthor");
		Assert.assertEquals(fetchedBook.getBookLanguage(), "testLanguage");
		Assert.assertEquals(fetchedBook.getPages(), 0);
	}
	
	@Test
	public void canUpdateBookTest() {
		
		testBook.setTitle("updatedBook");
		bookService.save(testBook);
		Book fetchedBook = bookService.get(testBook.getId());
		Assert.assertEquals(fetchedBook.getTitle(), "updatedBook");
	}
	
	@Test
	public void canDeleteBookTest() {
		
		bookService.delete(testBook.getId());
		Book fetchedBook = bookService.get(testBook.getId());
		Assert.assertNull(fetchedBook);
	}
	
	@Test
	public void canFetchUserTest() {
		
		User fetchedUser = userService.get(testUser.getId());
		
		Assert.assertEquals(fetchedUser.getEmail(), "test@gmail.com");
		Assert.assertEquals(fetchedUser.getName(), "test");
		Assert.assertEquals(fetchedUser.getInfo(), "test");
		
	}
	
	@Test
	public void canUpdateUserTest() {
		
		testUser.setName("updatedUser");
		userService.save(testUser);
		User fetchedUser = userService.get(testUser.getId());
		Assert.assertEquals(fetchedUser.getName(), "updatedUser");
	}
	
	@Test
	public void canDeleteUserTest() {
		
		userService.delete(testUser);
		User fetchedUser = userService.get(testUser.getId());
		Assert.assertNull(fetchedUser);
	}
	
	
	@Test
	public void canFetchCategoryTest() {
		
		Category fetchedCategory = categoryService.get(testCategory.getId());
		Assert.assertEquals(fetchedCategory.getName(), "test");
		
	}
	
	@Test
	public void canUpdateCategoryTest() {
		
		testCategory.setName("updatedCategory");
		categoryService.save(testCategory);
		Category fetchedCategory = categoryService.get(testCategory.getId());
		Assert.assertEquals(fetchedCategory.getName(), "updatedCategory");
	}
	
	@Test
	public void canDeleteCategoryTest() {
		
		categoryService.delete(testCategory);
		Category fetchedCategory = categoryService.get(testCategory.getId());
		Assert.assertNull(fetchedCategory);
	}
	
	
	@After
    public void removeAll() {
       
		bookService.delete(testBook);
		userService.delete(testUser);
		categoryService.delete(testCategory);
    }

}
