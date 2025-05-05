package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getAllTables() {
        String sql = "SHOW TABLES";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public List<Map<String, Object>> getTableColumns(String tableName) {
        logger.info("Richiesta colonne per tabella: {}", tableName);

        String sql = "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_TYPE, COLUMN_KEY " +
                "FROM information_schema.COLUMNS " +
                "WHERE TABLE_NAME = ? AND TABLE_SCHEMA = DATABASE()";

        logger.info("Query SQL eseguita: {}", sql);

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, tableName);

        logger.info("Numero colonne trovate: {}", result.size());
        logger.debug("Dettagli colonne: {}", result);

        return result;
    }

    public String getTableDDL(String tableName) {
        String sql = "SHOW CREATE TABLE " + tableName;
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getString(2));
    }

    public String addColumn(String tableName, Map<String, String> columnDef) {
        String columnName = columnDef.get("name");
        String dataType = columnDef.get("type");
        String nullable = Boolean.parseBoolean(columnDef.getOrDefault("nullable", "true")) ? "" : "NOT NULL";
        String sql = String.format("ALTER TABLE %s ADD COLUMN %s %s %s", tableName, columnName, dataType, nullable);
        jdbcTemplate.execute(sql);
        return "Colonna aggiunta con successo.";
    }

    public String deleteColumn(String tableName, String columnName) {
        String sql = String.format("ALTER TABLE %s DROP COLUMN %s", tableName, columnName);
        jdbcTemplate.execute(sql);
        return "Colonna eliminata con successo.";
    }

    public String generateDDLModification(String tableName, Map<String, Object> modifications) {
        StringBuilder ddl = new StringBuilder();
        ddl.append("ALTER TABLE ").append(tableName);

        List<Map<String, Object>> addColumns = (List<Map<String, Object>>) modifications.get("addColumns");
        List<String> dropColumns = (List<String>) modifications.get("dropColumns");

        List<String> ddlParts = new ArrayList<>();

        if (addColumns != null) {
            for (Map<String, Object> col : addColumns) {
                String name = (String) col.get("name");
                String type = (String) col.get("type");
                Boolean nullable = (Boolean) col.getOrDefault("nullable", true);
                String nullability = nullable ? "" : " NOT NULL";
                ddlParts.add(String.format("ADD COLUMN %s %s%s", name, type, nullability));
            }
        }

        if (dropColumns != null) {
            for (String colName : dropColumns) {
                ddlParts.add(String.format("DROP COLUMN %s", colName));
            }
        }

        ddl.append(" ").append(String.join(", ", ddlParts)).append(";");

        return ddl.toString();
    }


    public String applyModifications(String tableName, Map<String, Object> modifications) {
        String ddl = generateDDLModification(tableName, modifications);
        jdbcTemplate.execute(ddl);
        return "Modifiche applicate con successo.";
    }

}

