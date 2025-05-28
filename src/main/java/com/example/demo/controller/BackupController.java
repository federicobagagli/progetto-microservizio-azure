package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.RecordRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.Record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/backup")
public class BackupController {

    private static final Logger logger = LoggerFactory.getLogger(BackupController.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RecordRepository recordRepository;


    @GetMapping("/books/export")
    public ResponseEntity<?> exportBooks() {
        return ResponseEntity.ok(bookRepository.findAll());
    }

    @GetMapping("/records/export")
    public ResponseEntity<?> exportRecords() {
        return ResponseEntity.ok(recordRepository.findAll());
    }

    @GetMapping("/books/export/user")
    public ResponseEntity<?> exportBooksForUser(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        List<Book> books = bookRepository.findByUser(username);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/records/export/user")
    public ResponseEntity<?> exportRecordsForUser(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        List<Record> records = recordRepository.findByUser(username);
        return ResponseEntity.ok(records);
    }

    @PostMapping("/books/import")
    public ResponseEntity<?> importBooks(@RequestBody List<Object> data) {
        try {
            List<Book> books = data.stream()
                    .map(obj -> mapper.convertValue(obj, Book.class))
                    .collect(Collectors.toList());
            bookRepository.saveAll(books);
            return ResponseEntity.ok("Libri importati con successo.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body("Errore importazione libri: " + e.getMessage());
        }
    }

    @PostMapping("/records/import")
    public ResponseEntity<?> importRecords(@RequestBody List<Object> data) {
        try {
            List<Record> records = data.stream()
                    .map(obj -> mapper.convertValue(obj, Record.class))
                    .collect(Collectors.toList());
            recordRepository.saveAll(records);
            return ResponseEntity.ok("Dischi importati con successo.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body("Errore importazione dischi: " + e.getMessage());
        }
    }

    @PostMapping("/books/import/user")
    public ResponseEntity<?> importBooksForUser(@RequestHeader("Authorization") String token,
                                                @RequestBody List<Object> data) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        List<Book> books = data.stream()
                .map(obj -> {
                    Book b = mapper.convertValue(obj, Book.class);
                    b.setUser(username);
                    return b;
                }).collect(Collectors.toList());
        bookRepository.saveAll(books);
        return ResponseEntity.ok("Libri importati per utente " + username);
    }

    @PostMapping("/records/import/user")
    public ResponseEntity<?> importRecordsForUser(@RequestHeader("Authorization") String token,
                                                  @RequestBody List<Object> data) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        List<Record> records = data.stream()
                .map(obj -> {
                    Record r = mapper.convertValue(obj, Record.class);
                    r.setUser(username);
                    return r;
                }).collect(Collectors.toList());
        recordRepository.saveAll(records);
        return ResponseEntity.ok("Dischi importati per utente " + username);
    }

}

