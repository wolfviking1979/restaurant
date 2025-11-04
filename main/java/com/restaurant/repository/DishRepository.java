package com.restaurant.repository;

import com.restaurant.entity.Dish;
import com.restaurant.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    Optional<Dish> findByDishName(String dishName);

    List<Dish> findByCategory(MenuCategory category);

    List<Dish> findByIsActiveTrue();

    List<Dish> findByIsOnPromotionTrueAndIsActiveTrue();

    List<Dish> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Dish> findByDishNameContainingIgnoreCaseAndIsActiveTrue(String dishName);

    @Query("SELECT d FROM Dish d WHERE d.category.id = :categoryId AND d.isActive = true")
    List<Dish> findByCategoryIdAndActive(@Param("categoryId") Long categoryId);

    @Query("SELECT d FROM Dish d WHERE d.isActive = true " +
            "AND (LOWER(d.dishName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(d.composition) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Dish> searchDishes(@Param("searchTerm") String searchTerm);

    @Query("SELECT d.category, COUNT(d) FROM Dish d WHERE d.isActive = true GROUP BY d.category")
    List<Object[]> countDishesByCategory();

    @Query("SELECT d FROM Dish d WHERE d.isActive = true ORDER BY d.createdAt DESC")
    List<Dish> findRecentDishes(Pageable pageable);

    boolean existsByDishNameAndIdNot(String dishName, Long id);
}