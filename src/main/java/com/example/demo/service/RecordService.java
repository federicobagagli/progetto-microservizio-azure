package com.example.demo.service;

import com.example.demo.entity.Record;
import com.example.demo.repository.RecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public List<Record> findRecordsWithFilters(
            String cdNumber,
            String drawer,
            String composerAuthor,
            String albumTitle,
            String trackTitle,
            String ensemble,
            String compositionDate,
            String performers,
            String genre
    ) {
        Specification<Record> spec = Specification.where(null);

        if (cdNumber != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("cdNumber"), "%" + cdNumber + "%"));

        if (drawer != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("drawer"), "%" + drawer + "%"));

        if (composerAuthor != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("composerAuthor"), "%" + composerAuthor + "%"));

        if (albumTitle != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("albumTitle"), "%" + albumTitle + "%"));

        if (trackTitle != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("trackTitle"), "%" + trackTitle + "%"));

        if (ensemble != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("ensemble"), "%" + ensemble + "%"));

        if (compositionDate != null) {
            if (compositionDate.length() == 4) {
                try {
                    int yearValue = Integer.parseInt(compositionDate);
                    spec = spec.and((root, query, cb) ->
                            cb.equal(cb.function("YEAR", Integer.class, root.get("compositionDate")), yearValue));
                } catch (NumberFormatException ignored) {}
            } else {
                try {
                    Date exactDate = new SimpleDateFormat("yyyy-MM-dd").parse(compositionDate);
                    spec = spec.and((root, query, cb) ->
                            cb.equal(root.get("compositionDate"), exactDate));
                } catch (ParseException ignored) {}
            }
        }

        if (performers != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("performers"), "%" + performers + "%"));

        if (genre != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("genre"), "%" + genre + "%"));

        return recordRepository.findAll(spec);
    }


    public List<Record> findRecordsWithFiltersAndUser(
            String cdNumber,
            String drawer,
            String composerAuthor,
            String albumTitle,
            String trackTitle,
            String ensemble,
            String compositionDate,
            String performers,
            String genre,
            String username
    ) {
        Specification<Record> spec = Specification.where((root, query, cb) -> cb.equal(root.get("user"), username));

        if (cdNumber != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("cdNumber"), "%" + cdNumber + "%"));

        if (drawer != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("drawer"), "%" + drawer + "%"));

        if (composerAuthor != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("composerAuthor"), "%" + composerAuthor + "%"));

        if (albumTitle != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("albumTitle"), "%" + albumTitle + "%"));

        if (trackTitle != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("trackTitle"), "%" + trackTitle + "%"));

        if (ensemble != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("ensemble"), "%" + ensemble + "%"));

        if (compositionDate != null) {
            if (compositionDate.length() == 4) {
                try {
                    int yearValue = Integer.parseInt(compositionDate);
                    spec = spec.and((root, query, cb) ->
                            cb.equal(cb.function("YEAR", Integer.class, root.get("compositionDate")), yearValue));
                } catch (NumberFormatException ignored) {}
            } else {
                try {
                    Date exactDate = new SimpleDateFormat("yyyy-MM-dd").parse(compositionDate);
                    spec = spec.and((root, query, cb) ->
                            cb.equal(root.get("compositionDate"), exactDate));
                } catch (ParseException ignored) {}
            }
        }

        if (performers != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("performers"), "%" + performers + "%"));

        if (genre != null)
            spec = spec.and((root, query, cb) -> cb.like(root.get("genre"), "%" + genre + "%"));

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
