package com.example.demo;

import com.example.demo.service.DatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseServiceTest {

    @InjectMocks
    private DatabaseService databaseService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTables() {
        List<String> fakeTables = Arrays.asList("books", "authors");
        when(jdbcTemplate.queryForList("SHOW TABLES", String.class)).thenReturn(fakeTables);

        List<String> result = databaseService.getAllTables();

        assertEquals(2, result.size());
        assertTrue(result.contains("books"));
        verify(jdbcTemplate, times(1)).queryForList("SHOW TABLES", String.class);
    }

    @Test
    void testGetTableColumns() {
        Map<String, Object> col1 = Map.of("COLUMN_NAME", "id", "DATA_TYPE", "INT");
        Map<String, Object> col2 = Map.of("COLUMN_NAME", "title", "DATA_TYPE", "VARCHAR");
        when(jdbcTemplate.queryForList(anyString(), eq("books")))
                .thenReturn(Arrays.asList(col1, col2));

        List<Map<String, Object>> result = databaseService.getTableColumns("books");

        assertEquals(2, result.size());
        assertEquals("id", result.get(0).get("COLUMN_NAME"));
        verify(jdbcTemplate).queryForList(anyString(), eq("books"));
    }

    @Test
    void testGetTableDDL() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
                .thenReturn("CREATE TABLE books (id INT, title VARCHAR(255))");

        String ddl = databaseService.getTableDDL("books");

        assertTrue(ddl.contains("CREATE TABLE books"));
    }

    @Test
    void testAddColumn() {
        // non lancia eccezione → successo
        doNothing().when(jdbcTemplate).execute(anyString());

        String response = databaseService.addColumn("books",
                Map.of("name", "new_col", "type", "VARCHAR(50)", "nullable", "true"));

        assertEquals("Colonna aggiunta con successo.", response);
        verify(jdbcTemplate).execute(contains("ADD COLUMN new_col"));
    }

    @Test
    void testDeleteColumn() {
        doNothing().when(jdbcTemplate).execute(anyString());

        String response = databaseService.deleteColumn("books", "old_col");

        assertEquals("Colonna eliminata con successo.", response);
        verify(jdbcTemplate).execute(contains("DROP COLUMN old_col"));
    }

    @Test
    void testGenerateDDLModification() {
        Map<String, Object> modifications = Map.of(
                "addColumns", List.of(Map.of("name", "new_col", "type", "INT", "nullable", false)),
                "dropColumns", List.of("old_col")
        );

        String ddl = databaseService.generateDDLModification("books", modifications);

        assertTrue(ddl.contains("ADD COLUMN new_col INT NOT NULL"));
        assertTrue(ddl.contains("DROP COLUMN old_col"));
    }
}
