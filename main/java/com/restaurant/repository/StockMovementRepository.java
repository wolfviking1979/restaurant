package com.restaurant.repository;

import com.restaurant.entity.Ingredient;
import com.restaurant.entity.Order;
import com.restaurant.entity.StockMovement;
import com.restaurant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByIngredient(Ingredient ingredient);

    List<StockMovement> findByMovementType(StockMovement.MovementType movementType);

    List<StockMovement> findByReason(StockMovement.MovementReason reason);

    List<StockMovement> findByPerformedBy(User user);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementDate BETWEEN :startDate AND :endDate")
    List<StockMovement> findByMovementDateBetween(@Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);

    @Query("SELECT sm.ingredient, SUM(sm.quantity) FROM StockMovement sm " +
            "WHERE sm.movementType = 'OUTCOME' AND sm.movementDate BETWEEN :start AND :end " +
            "GROUP BY sm.ingredient " +
            "ORDER BY SUM(sm.quantity) DESC")
    List<Object[]> getIngredientConsumptionBetweenDates(@Param("start") LocalDateTime start,
                                                        @Param("end") LocalDateTime end);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.relatedOrder = :order")
    List<StockMovement> findByRelatedOrder(@Param("order") Order order);

    @Query("SELECT EXTRACT(MONTH FROM sm.movementDate) as month, " +
            "EXTRACT(YEAR FROM sm.movementDate) as year, " +
            "sm.movementType, " +
            "SUM(sm.quantity) as total " +
            "FROM StockMovement sm " +
            "WHERE sm.movementDate BETWEEN :start AND :end " +
            "GROUP BY EXTRACT(YEAR FROM sm.movementDate), EXTRACT(MONTH FROM sm.movementDate), sm.movementType " +
            "ORDER BY year, month")
    List<Object[]> getMonthlyStockMovementSummary(@Param("start") LocalDateTime start,
                                                  @Param("end") LocalDateTime end);
}