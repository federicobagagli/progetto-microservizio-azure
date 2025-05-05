package com.example.demo;


import com.example.demo.controller.DatabaseController;
import com.example.demo.service.DatabaseService;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DatabaseController.class)
@AutoConfigureMockMvc(addFilters = false)
class DatabaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatabaseService databaseService;

    @Test
    void testGetTables() throws Exception {
        when(databaseService.getAllTables()).thenReturn(Arrays.asList("books", "authors"));

        mockMvc.perform(get("/api/database/tables"))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"books\",\"authors\"]"));
    }

    @Test
    void testGetTableColumns() throws Exception {
        List<Map<String, Object>> columns = List.of(
                Map.of("COLUMN_NAME", "id", "DATA_TYPE", "INT"),
                Map.of("COLUMN_NAME", "title", "DATA_TYPE", "VARCHAR")
        );
        when(databaseService.getTableColumns("books")).thenReturn(columns);

        mockMvc.perform(get("/api/database/tables/books/columns"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTableDDL() throws Exception {
        when(databaseService.getTableDDL("books")).thenReturn("CREATE TABLE books (...)");

        mockMvc.perform(get("/api/database/tables/books/ddl"))
                .andExpect(status().isOk())
                .andExpect(content().string("CREATE TABLE books (...)"));
    }
}

