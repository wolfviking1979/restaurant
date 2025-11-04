package com.restaurant.repository;

import com.restaurant.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByTable(RestaurantTable table);

    List<Order> findByWaiter(User waiter);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByReservation(Reservation reservation);

    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.status.statusName = :statusName")
    List<Order> findByStatusName(@Param("statusName") String statusName);

    @Query("SELECT o FROM Order o JOIN o.table t WHERE t.tableNumber = :tableNumber")
    List<Order> findByTableNumber(@Param("tableNumber") String tableNumber);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status.statusName = 'оплачен' AND o.createdAt BETWEEN :start AND :end")
    Long countPaidOrdersBetween(@Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end);
}