package com.restaurant.repository;

import com.restaurant.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    Optional<RestaurantTable> findByTableNumber(String tableNumber);

    List<RestaurantTable> findByIsActiveTrue();

    List<RestaurantTable> findByCapacityGreaterThanEqual(Integer minCapacity);

    List<RestaurantTable> findByTableType(RestaurantTable.TableType tableType);

    List<RestaurantTable> findByCapacityBetween(Integer minCapacity, Integer maxCapacity);

    @Query("SELECT t FROM RestaurantTable t WHERE t.isActive = true AND t.capacity >= :minCapacity")
    List<RestaurantTable> findAvailableTablesByCapacity(@Param("minCapacity") Integer minCapacity);

    @Query("SELECT t FROM RestaurantTable t WHERE t.isActive = true " +
            "AND t.id NOT IN (" +
            "    SELECT r.table.id FROM Reservation r " +
            "    WHERE r.status IN ('PENDING', 'CONFIRMED') " +
            "    AND :requestedTime BETWEEN FUNCTION('TIMESTAMP', r.reservationDate, r.reservationTime) " +
            "    AND FUNCTION('TIMESTAMP', r.reservationDate, r.reservationTime) + (r.durationMinutes * 60)" +
            ")")
    List<RestaurantTable> findAvailableTablesAtTime(@Param("requestedTime") LocalDateTime requestedTime);

    @Query("SELECT t FROM RestaurantTable t WHERE t.isActive = true " +
            "AND t.capacity >= :guestsCount " +
            "AND t.id NOT IN (" +
            "    SELECT r.table.id FROM Reservation r " +
            "    WHERE r.status IN ('PENDING', 'CONFIRMED') " +
            "    AND (:startTime < FUNCTION('TIMESTAMP', r.reservationDate, r.reservationTime) + (r.durationMinutes * 60) " +
            "    AND :endTime > FUNCTION('TIMESTAMP', r.reservationDate, r.reservationTime))" +
            ")")
    List<RestaurantTable> findAvailableTables(@Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime,
                                              @Param("guestsCount") Integer guestsCount);

    @Query("SELECT COUNT(t) FROM RestaurantTable t WHERE t.isActive = true")
    long countActiveTables();

    @Query("SELECT t.tableType, COUNT(t) FROM RestaurantTable t WHERE t.isActive = true GROUP BY t.tableType")
    List<Object[]> countTablesByType();

    boolean existsByTableNumber(String tableNumber);
}