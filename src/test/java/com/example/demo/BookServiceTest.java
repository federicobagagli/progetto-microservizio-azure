package com.example.demo;

import com.example.demo.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.example.demo.service.BookService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DemoApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    public void testAddBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setGenre("Test Genre");
        book.setPublishDate(new Date());

        System.out.println("Before saving book");
        Book savedBook = bookService.addBook(book);
        System.out.println("After saving book");
        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
    }
}
