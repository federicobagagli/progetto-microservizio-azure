package com.example.demo.service;

import com.example.demo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.BookRepository;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }

    public List<Book> getBooksByPublishDateRange(Date startDate, Date endDate) {
        return bookRepository.findByPublishDateBetween(startDate, endDate);
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        if (bookRepository.existsById(id)) {
            bookDetails.setId(id);
            return bookRepository.save(bookDetails);
        }
        return null;
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> getFilteredBooks(String title, String author, String genre, Integer year) {
        // Qui si applicano i filtri. Se un filtro è null o vuoto, non lo prendiamo in considerazione.
        if (StringUtils.isEmpty(title) && StringUtils.isEmpty(author) && StringUtils.isEmpty(genre) && year == null) {
            return bookRepository.findAll(); // Restituisce tutti i libri se non ci sono filtri
        }
        return bookRepository.findBooksWithFilters(title, author, genre, year);
    }
}
