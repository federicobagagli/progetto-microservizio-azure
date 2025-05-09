package com.example.demo.service;

import com.example.demo.entity.Record;
import com.example.demo.repository.RecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RecordService {
    private static final Logger logger = LoggerFactory.getLogger(RecordService.class);

    private final RecordRepository recordRepository;

    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public List<Record> getAllRecords() {
        return recordRepository.findAll();
    }

    public Optional<Record> getRecordById(Long id) {
        return recordRepository.findById(id);
    }

    public List<Record> getRecordsByComposer(String composerAuthor) {
        return recordRepository.findByComposerAuthor(composerAuthor);
    }

    public List<Record> getRecordsByGenre(String genre) {
        return recordRepository.findByGenre(genre);
    }

    public List<Record> getRecordsByCompositionYearRange(Date startDate, Date endDate) {
        return recordRepository.findByCompositionDateBetween(startDate, endDate);
    }

    public Record addRecord(Record record) {
        return recordRepository.save(record);
    }

    public Record updateRecord(Long id, Record recordDetails) {
        if (recordRepository.existsById(id)) {
            recordDetails.setId(id);
            return recordRepository.save(recordDetails);
        }
        return null;
    }

    public void deleteRecord(Long id, String username) {
        Optional<Record> recordOpt = recordRepository.findById(id);
        if (recordOpt.isPresent()) {
            Record record = recordOpt.get();
            if (username == null || username.equals(record.getUser())) {
                recordRepository.deleteById(id);
            } else {
                throw new RuntimeException("You cannot delete a record that doesn't belong to you.");
            }
        }
    }

    public List<Record> findRecordsWithFilters(String albumTitle, String composerAuthor, String genre, Integer year) {
        Specification<Record> spec = Specification.where(null);

        logger.info("Running search with filters:");
        logger.info("Album Title: {}", albumTitle);
        logger.info("Composer/Author: {}", composerAuthor);
        logger.info("Genre: {}", genre);
        logger.info("Year: {}", year);

        if (albumTitle != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("albumTitle"), "%" + albumTitle + "%"));
        }

        if (composerAuthor != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("composerAuthor"), "%" + composerAuthor + "%"));
        }

        if (genre != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("genre"), "%" + genre + "%"));
        }

        if (year != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.function("YEAR", Integer.class, root.get("compositionDate")), year));
        }

        List<Record> records = recordRepository.findAll(spec);
        logger.info("Records found: {}", records.size());
        return records;
    }

    public List<Record> findRecordsWithFiltersAndUser(String albumTitle, String composerAuthor, String genre, Integer year, String username) {
        Specification<Record> spec = Specification.where((root, query, cb) ->
                cb.equal(root.get("user"), username));

        if (albumTitle != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("albumTitle"), "%" + albumTitle + "%"));
        }

        if (composerAuthor != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("composerAuthor"), "%" + composerAuthor + "%"));
        }

        if (genre != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("genre"), "%" + genre + "%"));
        }

        if (year != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.function("YEAR", Integer.class, root.get("compositionDate")), year));
        }

        return recordRepository.findAll(spec);
    }

    public List<Record> getAllRecordsByUser(String user) {
        return recordRepository.findByUser(user);
    }

    public List<Record> getRecordsByComposerAndUser(String composerAuthor, String user) {
        return recordRepository.findByComposerAuthorAndUser(composerAuthor, user);
    }

    public List<Record> getRecordsByGenreAndUser(String genre, String user) {
        return recordRepository.findByGenreAndUser(genre, user);
    }
}
