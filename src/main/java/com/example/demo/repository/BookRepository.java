package com.example.demo.repository;

import com.example.demo.entity.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthor(String author);
    List<Book> findByGenre(String genre);
    List<Book> findByPublishDateBetween(Date startDate, Date endDate);

    List<Book> findByUser(String user);

    List<Book> findByAuthorAndUser(String author, String user);

    List<Book> findByGenreAndUser(String genre, String user);

    List<Book> findAll(Specification<Book> spec);



}
