package com.example.demo;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    private final BookRepository bookRepository = Mockito.mock(BookRepository.class);
    private final BookService bookService = new BookService();

    {
        // Manual injection (no @Autowired in unit test)
        bookService.setBookRepository(bookRepository);
    }

    @Test
    void getBooksByAuthorAndUser_shouldReturnBooks() {
        Book book1 = new Book();
        book1.setAuthor("Author A");
        book1.setUser("user1");

        Book book2 = new Book();
        book2.setAuthor("Author A");
        book2.setUser("user1");

        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findByAuthorAndUser("Author A", "user1")).thenReturn(books);

        List<Book> result = bookService.getBooksByAuthorAndUser("Author A", "user1");

        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findByAuthorAndUser("Author A", "user1");
    }

    @Test
    void getBooksByGenreAndUser_shouldReturnBooks() {
        Book book1 = new Book();
        book1.setGenre("Fantasy");
        book1.setUser("user2");

        when(bookRepository.findByGenreAndUser("Fantasy", "user2")).thenReturn(List.of(book1));

        List<Book> result = bookService.getBooksByGenreAndUser("Fantasy", "user2");

        assertEquals(1, result.size());
        assertEquals("Fantasy", result.get(0).getGenre());
        verify(bookRepository).findByGenreAndUser("Fantasy", "user2");
    }
}
