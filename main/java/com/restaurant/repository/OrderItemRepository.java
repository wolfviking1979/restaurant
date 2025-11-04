package com.restaurant.repository;

import com.restaurant.entity.Dish;
import com.restaurant.entity.Order;
import com.restaurant.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);

    List<OrderItem> findByDish(Dish dish);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT oi.dish, SUM(oi.quantity) as totalQuantity " +
            "FROM OrderItem oi " +
            "WHERE oi.order.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY oi.dish " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> findPopularDishesBetweenDates(@Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);
}