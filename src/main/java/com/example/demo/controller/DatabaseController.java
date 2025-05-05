package com.example.demo.controller;

import com.example.demo.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    // 1️⃣ Ottieni la lista delle tabelle
    @GetMapping("/tables")
    public List<String> getTables() {
        return databaseService.getAllTables();
    }

    // 2️⃣ Ottieni le colonne di una tabella
    @GetMapping("/tables/{tableName}/columns")
    public List<Map<String, Object>> getTableColumns(@PathVariable String tableName) {
        return databaseService.getTableColumns(tableName);
    }

    // 3️⃣ Ottieni la DDL di una tabella
    @GetMapping("/tables/{tableName}/ddl")
    public String getTableDDL(@PathVariable String tableName) {
        return databaseService.getTableDDL(tableName);
    }

    // 4️⃣ Aggiungi una colonna
    @PostMapping("/tables/{tableName}/addColumn")
    public String addColumn(@PathVariable String tableName, @RequestBody Map<String, String> columnDefinition) {
        return databaseService.addColumn(tableName, columnDefinition);
    }

    // 5️⃣ Elimina una colonna
    @DeleteMapping("/tables/{tableName}/columns/{columnName}")
    public String deleteColumn(@PathVariable String tableName, @PathVariable String columnName) {
        return databaseService.deleteColumn(tableName, columnName);
    }

    // 6️⃣ Genera DDL di modifica (ma non esegue)
    @PostMapping("/tables/{tableName}/generateDDLModification")
    public String generateDDLModification(@PathVariable String tableName, @RequestBody Map<String, Object> modifications) {
        return databaseService.generateDDLModification(tableName, modifications);
    }

    // 7️⃣ Applica modifiche direttamente
    @PostMapping("/tables/{tableName}/applyModifications")
    public String applyModifications(@PathVariable String tableName, @RequestBody Map<String, Object> modifications) {
        return databaseService.applyModifications(tableName, modifications);
    }
}
    //     }