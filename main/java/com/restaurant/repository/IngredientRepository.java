package com.restaurant.repository;

import com.restaurant.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByIngredientName(String ingredientName);

    List<Ingredient> findByCurrentStockLessThanEqual(BigDecimal threshold);

    @Query("SELECT i FROM Ingredient i WHERE i.currentStock <= i.minStockLevel")
    List<Ingredient> findLowStockIngredients();

    @Query("SELECT i FROM Ingredient i WHERE i.currentStock = 0")
    List<Ingredient> findOutOfStockIngredients();

    boolean existsByIngredientName(String ingredientName);

    @Query("SELECT i FROM Ingredient i WHERE LOWER(i.ingredientName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Ingredient> findByIngredientNameContainingIgnoreCase(@Param("name") String name);
}