package com.example.demo.repository;

import com.example.demo.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, JpaSpecificationExecutor<Record> {

    List<Record> findByComposerAuthor(String composerAuthor);

    List<Record> findByGenre(String genre);

    List<Record> findByUser(String user);

    List<Record> findByComposerAuthorAndUser(String composerAuthor, String user);

    List<Record> findByGenreAndUser(String genre, String user);

    List<Record> findByCompositionDateBetween(Date startDate, Date endDate);
}
