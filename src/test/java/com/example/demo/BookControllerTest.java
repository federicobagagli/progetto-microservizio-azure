package com.example.demo;


import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    void userShouldGetFilteredBooks() throws Exception {
        Mockito.when(bookService.findBooksWithFiltersAndUser(any(), any(), any(), any(), eq("user1")))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/books")
                        .param("title", "java"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminShouldGetAllFilteredBooks() throws Exception {
        Mockito.when(bookService.findBooksWithFilters(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/books")
                        .param("title", "spring"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    void userShouldCreateBookAssignedToSelf() throws Exception {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Author");
        book.setGenre("Genre");

        Mockito.when(bookService.addBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/api/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk());
    }
}

