package com.example.demo.controller;

import com.example.demo.entity.Record;
import com.example.demo.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping
    public List<Record> getRecords(
            @RequestParam(required = false) String cdNumber,
            @RequestParam(required = false) String drawer,
            @RequestParam(required = false) String composerAuthor,
            @RequestParam(required = false) String albumTitle,
            @RequestParam(required = false) String trackTitle,
            @RequestParam(required = false) String ensemble,
            @RequestParam(required = false) String compositionDate,
            @RequestParam(required = false) String performers,
            @RequestParam(required = false) String genre
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return isAdmin
                ? recordService.findRecordsWithFilters(cdNumber, drawer, composerAuthor, albumTitle, trackTitle, ensemble, compositionDate, performers, genre)
                : recordService.findRecordsWithFiltersAndUser(cdNumber, drawer, composerAuthor, albumTitle, trackTitle, ensemble, compositionDate, performers, genre, username);
    }


    @GetMapping("/all")
    public List<Record> getAllRecords() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return isAdmin ? recordService.getAllRecords() : recordService.getAllRecordsByUser(username);
    }

    @GetMapping("/{id}")
    public Optional<Record> getRecordById(@PathVariable Long id) {
        return recordService.getRecordById(id);
    }

    @GetMapping("/composer/{composerAuthor}")
    public List<Record> getRecordsByComposer(@PathVariable String composerAuthor) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return isAdmin ? recordService.getRecordsByComposer(composerAuthor)
                : recordService.getRecordsByComposerAndUser(composerAuthor, username);
    }

    @GetMapping("/genre/{genre}")
    public List<Record> getRecordsByGenre(@PathVariable String genre) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return isAdmin ? recordService.getRecordsByGenre(genre)
                : recordService.getRecordsByGenreAndUser(genre, username);
    }

    @GetMapping("/compositionYear")
    public List<Record> getRecordsByCompositionYearRange(
            @RequestParam String startDate,
            @RequestParam String endDate) throws ParseException {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        return recordService.getRecordsByCompositionYearRange(start, end);
    }

    @PostMapping("/create")
    public Record addRecord(@RequestBody Record record) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        record.setUser(username);
        return recordService.addRecord(record);
    }

    @PutMapping("/{id}")
    public Record updateRecord(@PathVariable Long id, @RequestBody Record recordDetails) {
        return recordService.updateRecord(id, recordDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteRecord(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        recordService.deleteRecord(id, isAdmin ? null : username);
    }
}
