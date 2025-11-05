package com.restaurant.controller;

import com.restaurant.dto.CommonDTO;
import com.restaurant.dto.ReservationDTO;
import com.restaurant.entity.Reservation;
import com.restaurant.entity.RestaurantTable;
import com.restaurant.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('WAITER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<ReservationDTO.ReservationResponse>> createReservation(
            @Valid @RequestBody ReservationDTO.CreateReservationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            ReservationDTO.ReservationResponse response = reservationService.createReservation(
                    request, userDetails.getUsername());
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Reservation created successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/date/{date}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('WAITER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<List<ReservationDTO.ReservationResponse>>> getReservationsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ReservationDTO.ReservationResponse> reservations = reservationService.getReservationsByDate(date);
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(reservations));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('WAITER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<List<ReservationDTO.ReservationResponse>>> getReservationsByStatus(
            @PathVariable Reservation.ReservationStatus status) {
        List<ReservationDTO.ReservationResponse> reservations = reservationService.getReservationsByStatus(status);
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(reservations));
    }

    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<ReservationDTO.ReservationResponse>> confirmReservation(
            @PathVariable Long id) {
        try {
            ReservationDTO.ReservationResponse response = reservationService.confirmReservation(id);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Reservation confirmed", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('MANAGER') or hasRole('WAITER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<ReservationDTO.ReservationResponse>> cancelReservation(
            @PathVariable Long id) {
        try {
            ReservationDTO.ReservationResponse response = reservationService.cancelReservation(id);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Reservation cancelled", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    // ИСПРАВЛЕННЫЙ МЕТОД - возвращаем List<RestaurantTable>
    @GetMapping("/available-tables")
    @PreAuthorize("hasRole('MANAGER') or hasRole('WAITER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<List<RestaurantTable>>> getAvailableTables(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
            @RequestParam Integer duration,
            @RequestParam Integer guestsCount) {
        try {
            List<RestaurantTable> availableTables = reservationService.getAvailableTables(date, time, duration, guestsCount);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(availableTables));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('WAITER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<ReservationDTO.ReservationResponse>> getReservationById(
            @PathVariable Long id) {
        try {
            ReservationDTO.ReservationResponse reservation = reservationService.getReservationById(id);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(reservation));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ
    @GetMapping("/guest/{phone}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('WAITER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<List<ReservationDTO.ReservationResponse>>> getReservationsByGuestPhone(
            @PathVariable String phone) {
        List<ReservationDTO.ReservationResponse> reservations = reservationService.getReservationsByGuestPhone(phone);
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(reservations));
    }
}