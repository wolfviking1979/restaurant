package com.restaurant.repository;

import com.restaurant.entity.Reservation;
import com.restaurant.entity.RestaurantTable;
import com.restaurant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByReservationDate(LocalDate date);

    List<Reservation> findByStatus(Reservation.ReservationStatus status);

    List<Reservation> findByGuestNameContainingIgnoreCase(String guestName);

    List<Reservation> findByGuestPhone(String guestPhone);

    List<Reservation> findByTable(RestaurantTable table);

    List<Reservation> findByCreatedBy(User user);

    @Query("SELECT r FROM Reservation r WHERE r.reservationDate BETWEEN :startDate AND :endDate")
    List<Reservation> findByReservationDateBetween(@Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    // Упрощенная версия без полных путей к enum
    @Query("SELECT r FROM Reservation r WHERE r.table.id = :tableId " +
            "AND (r.status = 'PENDING' OR r.status = 'CONFIRMED') " +
            "AND ((:startTime < FUNCTION('TIMESTAMP', r.reservationDate, r.reservationTime) + (r.durationMinutes * 60)) " +
            "AND (:endTime > FUNCTION('TIMESTAMP', r.reservationDate, r.reservationTime)))")
    List<Reservation> findConflictingReservations(@Param("tableId") Long tableId,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

    @Query("SELECT r FROM Reservation r WHERE r.reservationDate = :date " +
            "AND (r.status = 'PENDING' OR r.status = 'CONFIRMED') " +
            "ORDER BY r.reservationTime")
    List<Reservation> findActiveReservationsByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.reservationDate = :date " +
            "AND r.status = 'CONFIRMED'")
    long countConfirmedReservationsByDate(@Param("date") LocalDate date);

    @Query("SELECT r.table, COUNT(r) FROM Reservation r " +
            "WHERE r.reservationDate BETWEEN :startDate AND :endDate " +
            "AND r.status = 'CONFIRMED' " +
            "GROUP BY r.table " +
            "ORDER BY COUNT(r) DESC")
    List<Object[]> getPopularTables(@Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);

    // Дополнительные полезные методы
    List<Reservation> findByStatusIn(List<Reservation.ReservationStatus> statuses);

    @Query("SELECT r FROM Reservation r WHERE r.reservationDate = :date AND r.table = :table")
    List<Reservation> findByDateAndTable(@Param("date") LocalDate date,
                                         @Param("table") RestaurantTable table);
}