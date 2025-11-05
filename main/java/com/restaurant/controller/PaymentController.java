package com.restaurant.controller;

import com.restaurant.dto.CommonDTO;
import com.restaurant.dto.PaymentDTO;
import com.restaurant.service.PaymentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('WAITER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<PaymentDTO.PaymentResponse>> createPayment(
            @Valid @RequestBody PaymentDTO.CreatePaymentRequest request) {
        try {
            PaymentDTO.PaymentResponse response = paymentService.createPayment(request);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Payment created successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('WAITER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<List<PaymentDTO.PaymentResponse>>> getPaymentsByOrder(
            @PathVariable Long orderId) {
        List<PaymentDTO.PaymentResponse> payments = paymentService.getPaymentsByOrder(orderId);
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(payments));
    }

    @PostMapping("/{id}/process")
    @PreAuthorize("hasRole('WAITER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<PaymentDTO.PaymentResponse>> processPayment(
            @PathVariable Long id,
            @RequestParam String transactionId) {
        try {
            PaymentDTO.PaymentResponse response = paymentService.processPayment(id, transactionId);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Payment processed successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/statistics/revenue")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<PaymentDTO.RevenueStatistics>> getRevenueStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            PaymentDTO.RevenueStatistics statistics = paymentService.getRevenueStatistics(startDate, endDate);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(statistics));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }
}