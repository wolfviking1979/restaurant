package com.restaurant.repository;

import com.restaurant.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    Optional<OrderStatus> findByStatusName(String statusName);

    @Query("SELECT os FROM OrderStatus os WHERE os.statusName IN :statusNames")
    List<OrderStatus> findByStatusNames(@Param("statusNames") List<String> statusNames);
}