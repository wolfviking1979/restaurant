package com.restaurant.service;

import com.restaurant.DTO.ReservationDTO;
import com.restaurant.model.Reservation;
import com.restaurant.model.RestaurantTable;
import com.restaurant.model.User;
import com.restaurant.repository.ReservationRepository;
import com.restaurant.repository.RestaurantTableRepository;
import com.restaurant.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository tableRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              RestaurantTableRepository tableRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ReservationDTO.ReservationResponse createReservation(ReservationDTO.CreateReservationRequest request, String username) {
        RestaurantTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found"));

        // Проверка доступности столика
        if (!isTableAvailable(table.getId(), request.getReservationDate(), request.getReservationTime(), request.getDurationMinutes())) {
            throw new RuntimeException("Table is not available at the requested time");
        }

        // Проверка вместимости
        if (request.getGuestsCount() > table.getCapacity()) {
            throw new RuntimeException("Table capacity exceeded");
        }

        User createdBy = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reservation reservation = new Reservation();
        reservation.setGuestName(request.getGuestName());
        reservation.setGuestPhone(request.getGuestPhone());
        reservation.setGuestEmail(request.getGuestEmail());
        reservation.setReservationDate(request.getReservationDate());
        reservation.setReservationTime(request.getReservationTime());
        reservation.setDurationMinutes(request.getDurationMinutes());
        reservation.setGuestsCount(request.getGuestsCount());
        reservation.setTable(table);
        reservation.setStatus(Reservation.ReservationStatus.PENDING);
        reservation.setSpecialRequests(request.getSpecialRequests());
        reservation.setCreatedBy(createdBy);

        Reservation savedReservation = reservationRepository.save(reservation);
        return new ReservationDTO.ReservationResponse(savedReservation);
    }

    public boolean isTableAvailable(Long tableId, LocalDate date, LocalTime time, Integer duration) {
        LocalDateTime reservationStart = LocalDateTime.of(date, time);
        LocalDateTime reservationEnd = reservationStart.plusMinutes(duration);

        List<Reservation> conflictingReservations = reservationRepository
                .findConflictingReservations(tableId, reservationStart, reservationEnd);

        return conflictingReservations.isEmpty();
    }

    public List<ReservationDTO.ReservationResponse> getReservationsByDate(LocalDate date) {
        return reservationRepository.findByReservationDate(date).stream()
                .map(ReservationDTO.ReservationResponse::new)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO.ReservationResponse> getReservationsByStatus(Reservation.ReservationStatus status) {
        return reservationRepository.findByStatus(status).stream()
                .map(ReservationDTO.ReservationResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationDTO.ReservationResponse confirmReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        Reservation updatedReservation = reservationRepository.save(reservation);
        return new ReservationDTO.ReservationResponse(updatedReservation);
    }

    @Transactional
    public ReservationDTO.ReservationResponse cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        Reservation updatedReservation = reservationRepository.save(reservation);
        return new ReservationDTO.ReservationResponse(updatedReservation);
    }

    public List<RestaurantTable> getAvailableTables(LocalDate date, LocalTime time, Integer duration, Integer guestsCount) {
        LocalDateTime startTime = LocalDateTime.of(date, time);
        LocalDateTime endTime = startTime.plusMinutes(duration);

        return tableRepository.findAvailableTables(startTime, endTime, guestsCount);
    }

    public ReservationDTO.ReservationResponse getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        return new ReservationDTO.ReservationResponse(reservation);
    }

    public List<ReservationDTO.ReservationResponse> getReservationsByGuestPhone(String phone) {
        return reservationRepository.findByGuestPhone(phone).stream()
                .map(ReservationDTO.ReservationResponse::new)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO.ReservationResponse> getReservationsByGuestName(String guestName) {
        return reservationRepository.findByGuestNameContainingIgnoreCase(guestName).stream()
                .map(ReservationDTO.ReservationResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationDTO.ReservationResponse updateReservation(Long id, ReservationDTO.CreateReservationRequest request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        RestaurantTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found"));

        // Проверка доступности столика (исключая текущее бронирование)
        if (!isTableAvailableForUpdate(table.getId(), reservation.getId(), request.getReservationDate(), request.getReservationTime(), request.getDurationMinutes())) {
            throw new RuntimeException("Table is not available at the requested time");
        }

        reservation.setGuestName(request.getGuestName());
        reservation.setGuestPhone(request.getGuestPhone());
        reservation.setGuestEmail(request.getGuestEmail());
        reservation.setReservationDate(request.getReservationDate());
        reservation.setReservationTime(request.getReservationTime());
        reservation.setDurationMinutes(request.getDurationMinutes());
        reservation.setGuestsCount(request.getGuestsCount());
        reservation.setTable(table);
        reservation.setSpecialRequests(request.getSpecialRequests());

        Reservation updatedReservation = reservationRepository.save(reservation);
        return new ReservationDTO.ReservationResponse(updatedReservation);
    }

    private boolean isTableAvailableForUpdate(Long tableId, Long reservationId, LocalDate date, LocalTime time, Integer duration) {
        LocalDateTime reservationStart = LocalDateTime.of(date, time);
        LocalDateTime reservationEnd = reservationStart.plusMinutes(duration);

        List<Reservation> conflictingReservations = reservationRepository
                .findConflictingReservations(tableId, reservationStart, reservationEnd);

        // Исключаем текущее бронирование из проверки
        return conflictingReservations.stream()
                .allMatch(res -> res.getId().equals(reservationId));
    }

    public Map<String, Object> getReservationStatistics(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> stats = new HashMap<>();

        List<Reservation> reservations = reservationRepository.findByReservationDateBetween(startDate, endDate);

        long totalReservations = reservations.size();
        long confirmedReservations = reservations.stream()
                .filter(r -> r.getStatus() == Reservation.ReservationStatus.CONFIRMED)
                .count();
        long cancelledReservations = reservations.stream()
                .filter(r -> r.getStatus() == Reservation.ReservationStatus.CANCELLED)
                .count();

        stats.put("totalReservations", totalReservations);
        stats.put("confirmedReservations", confirmedReservations);
        stats.put("cancelledReservations", cancelledReservations);
        stats.put("confirmationRate", totalReservations > 0 ? (double) confirmedReservations / totalReservations : 0);
        stats.put("period", Map.of("start", startDate, "end", endDate));

        return stats;
    }
}