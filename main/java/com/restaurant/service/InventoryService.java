package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.DishRecipeRepository;
import com.restaurant.repository.IngredientRepository;
import com.restaurant.repository.StockMovementRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class InventoryService {

    private final IngredientRepository ingredientRepository;
    private final StockMovementRepository stockMovementRepository;
    private final DishRecipeRepository dishRecipeRepository;

    public InventoryService(IngredientRepository ingredientRepository,
                            StockMovementRepository stockMovementRepository,
                            DishRecipeRepository dishRecipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.dishRecipeRepository = dishRecipeRepository;
    }

    public List<Ingredient> getLowStockIngredients() {
        return ingredientRepository.findLowStockIngredients();
    }

    public void processStockMovement(Long ingredientId, BigDecimal quantity,
                                     StockMovement.MovementType movementType,
                                     StockMovement.MovementReason reason, String notes) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));

        StockMovement movement = new StockMovement();
        movement.setIngredient(ingredient);
        movement.setQuantity(quantity);
        movement.setMovementType(movementType);
        movement.setReason(reason);
        movement.setNotes(notes);
        movement.setMovementDate(LocalDateTime.now());

        stockMovementRepository.save(movement);
    }

    public boolean canProduceDish(Dish dish, Integer portions) {
        List<DishRecipe> recipes = dishRecipeRepository.findByDish(dish);
        return recipes.stream().allMatch(recipe -> recipe.canProduce(portions));
    }

    public Map<Ingredient, BigDecimal> calculateRequiredIngredients(List<OrderItem> orderItems) {
        Map<Ingredient, BigDecimal> requiredIngredients = new HashMap<>();

        for (OrderItem item : orderItems) {
            List<DishRecipe> recipes = dishRecipeRepository.findByDish(item.getDish());
            for (DishRecipe recipe : recipes) {
                BigDecimal totalRequired = recipe.getQuantityRequired()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));

                requiredIngredients.merge(recipe.getIngredient(), totalRequired, BigDecimal::add);
            }
        }

        return requiredIngredients;
    }
}