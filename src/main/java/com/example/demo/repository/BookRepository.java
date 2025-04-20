package com.example.demo.repository;

import com.example.demo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthor(String author);
    List<Book> findByGenre(String genre);
    List<Book> findByPublishDateBetween(Date startDate, Date endDate);



    @Query("SELECT b FROM Book b WHERE " +
            "(?1 IS NULL OR b.title LIKE %?1%) AND " +
            "(?2 IS NULL OR b.author LIKE %?2%) AND " +
            "(?3 IS NULL OR b.genre LIKE %?3%) AND " +
            "(?4 IS NULL OR b.year = ?4)")
    List<Book> findBooksWithFilters(String title, String author, String genre, Integer year);

}
