package com.restaurant.controller;

import com.restaurant.dto.CommonDTO;
import com.restaurant.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/revenue/daily")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<Map<String, Object>>> getDailyRevenueReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            Map<String, Object> report = reportService.getDailyRevenueReport(date);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(report));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/revenue/period")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<Map<String, Object>>> getRevenueReportByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Map<String, Object> report = reportService.getRevenueReportByPeriod(startDate, endDate);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(report));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/popular-dishes")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<Map<String, Object>>> getPopularDishesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Map<String, Object> report = reportService.getPopularDishesReport(startDate, endDate);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(report));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/table-occupancy")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<Map<String, Object>>> getTableOccupancyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Map<String, Object> report = reportService.getTableOccupancyReport(startDate, endDate);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(report));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/staff-efficiency")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<Map<String, Object>>> getStaffEfficiencyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Map<String, Object> report = reportService.getStaffEfficiencyReport(startDate, endDate);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(report));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/inventory")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<Map<String, Object>>> getInventoryReport() {
        try {
            Map<String, Object> report = reportService.getInventoryReport();
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(report));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }
}