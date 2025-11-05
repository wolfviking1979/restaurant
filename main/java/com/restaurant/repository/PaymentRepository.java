package com.restaurant.repository;

import com.restaurant.entity.Order;
import com.restaurant.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrder(Order order);

    List<Payment> findByPaymentStatus(Payment.PaymentStatus status);

    List<Payment> findByPaymentMethod(Payment.PaymentMethod method);

    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = 'PAID' AND p.createdAt BETWEEN :start AND :end")
    BigDecimal getTotalRevenueBetween(@Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);

    @Query("SELECT p.paymentMethod, SUM(p.amount) FROM Payment p " +
            "WHERE p.paymentStatus = 'PAID' AND p.createdAt BETWEEN :start AND :end " +
            "GROUP BY p.paymentMethod")
    List<Object[]> getRevenueByPaymentMethodBetween(@Param("start") LocalDateTime start,
                                                    @Param("end") LocalDateTime end);

    @Query("SELECT p FROM Payment p WHERE p.order.id = :orderId AND p.paymentStatus = 'PAID'")
    List<Payment> findSuccessfulPaymentsByOrder(@Param("orderId") Long orderId);

    // ДОБАВЛЯЕМ НЕДОСТАЮЩИЙ МЕТОД
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentStatus = 'PAID' AND p.createdAt BETWEEN :start AND :end")
    Long countSuccessfulPaymentsBetween(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);
}