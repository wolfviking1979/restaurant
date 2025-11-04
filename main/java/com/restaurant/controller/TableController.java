package com.restaurant.controller;

import com.restaurant.DTO.CommonDTO;
import com.restaurant.model.RestaurantTable;
import com.restaurant.service.TableService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tables")
@Validated
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CommonDTO.ApiResponse<RestaurantTable>> createTable(
            @Valid @RequestBody RestaurantTable table) {
        try {
            RestaurantTable createdTable = tableService.createTable(table);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Table created successfully", createdTable));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<CommonDTO.ApiResponse<List<RestaurantTable>>> getAllTables() {
        List<RestaurantTable> tables = tableService.getAllTables();
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(tables));
    }

    @GetMapping("/active")
    public ResponseEntity<CommonDTO.ApiResponse<List<RestaurantTable>>> getActiveTables() {
        List<RestaurantTable> tables = tableService.getActiveTables();
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(tables));
    }

    @GetMapping("/available")
    public ResponseEntity<CommonDTO.ApiResponse<List<RestaurantTable>>> getAvailableTables(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam Integer guestsCount) {
        try {
            List<RestaurantTable> tables = tableService.getAvailableTables(startTime, endTime, guestsCount);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(tables));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/capacity/{minCapacity}")
    public ResponseEntity<CommonDTO.ApiResponse<List<RestaurantTable>>> getTablesByCapacity(
            @PathVariable Integer minCapacity) {
        List<RestaurantTable> tables = tableService.getTablesByCapacity(minCapacity);
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(tables));
    }

    @GetMapping("/type/{tableType}")
    public ResponseEntity<CommonDTO.ApiResponse<List<RestaurantTable>>> getTablesByType(
            @PathVariable RestaurantTable.TableType tableType) {
        List<RestaurantTable> tables = tableService.getTablesByType(tableType);
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(tables));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CommonDTO.ApiResponse<RestaurantTable>> updateTable(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantTable tableDetails) {
        try {
            RestaurantTable updatedTable = tableService.updateTable(id, tableDetails);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Table updated successfully", updatedTable));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CommonDTO.ApiResponse<Void>> deactivateTable(@PathVariable Long id) {
        try {
            tableService.deactivateTable(id);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Table deactivated successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CommonDTO.ApiResponse<Void>> activateTable(@PathVariable Long id) {
        try {
            tableService.activateTable(id);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Table activated successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CommonDTO.ApiResponse<Map<String, Object>>> getTableStatistics() {
        Map<String, Object> statistics = tableService.getTableStatistics();
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(statistics));
    }
}