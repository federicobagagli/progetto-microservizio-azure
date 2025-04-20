package com.example.demo.service;

import com.example.demo.entity.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.demo.repository.BookRepository;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

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


    public List<Book> findBooksWithFilters(String title, String author, String genre, Integer year) {
        Specification<Book> spec = Specification.where(null);

        logger.info("Esecuzione ricerca con i seguenti filtri:");
        logger.info("Title: {}", title);
        logger.info("Author: {}", author);
        logger.info("Genre: {}", genre);
        logger.info("Year: {}", year);

        if (title != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("title"), "%" + title + "%"));
        }

        if (author != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("author"), "%" + author + "%"));
        }

        if (genre != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("genre"), "%" + genre + "%"));
        }

        if (year != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, root.get("publishDate")), year));
        }

        logger.info("Specifica creata: {}", spec);

        List<Book> books = bookRepository.findAll(spec);
        // Logga il risultato della query
        logger.info("Numero di libri trovati: {}", books.size());
        return books;
    }
}
