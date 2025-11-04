package com.restaurant.repository;

import com.restaurant.model.Dish;
import com.restaurant.model.DishRecipe;
import com.restaurant.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRecipeRepository extends JpaRepository<DishRecipe, Long> {

    List<DishRecipe> findByDish(Dish dish);

    List<DishRecipe> findByIngredient(Ingredient ingredient);

    Optional<DishRecipe> findByDishAndIngredient(Dish dish, Ingredient ingredient);

    @Query("SELECT dr FROM DishRecipe dr WHERE dr.dish.id = :dishId")
    List<DishRecipe> findByDishId(@Param("dishId") Long dishId);

    @Query("SELECT dr.ingredient, SUM(dr.quantityRequired) as totalRequired " +
            "FROM DishRecipe dr " +
            "WHERE dr.dish.id IN :dishIds " +
            "GROUP BY dr.ingredient")
    List<Object[]> getTotalIngredientsRequiredForDishes(@Param("dishIds") List<Long> dishIds);

    @Query("SELECT dr FROM DishRecipe dr WHERE dr.ingredient.currentStock < dr.quantityRequired")
    List<DishRecipe> findRecipesWithInsufficientIngredients();
}