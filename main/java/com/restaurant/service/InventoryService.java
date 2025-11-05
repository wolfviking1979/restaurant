package com.restaurant.service;

import com.restaurant.dto.InventoryDTO;
import com.restaurant.entity.Ingredient;
import com.restaurant.entity.StockMovement;
import com.restaurant.repository.IngredientRepository;
import com.restaurant.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryService {

    private final IngredientRepository ingredientRepository;
    private final StockMovementRepository stockMovementRepository;

    public InventoryService(IngredientRepository ingredientRepository,
                            StockMovementRepository stockMovementRepository) {
        this.ingredientRepository = ingredientRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    // Изменяем возвращаемый тип на List<Ingredient>
    public List<Ingredient> getLowStockIngredients() {
        return ingredientRepository.findLowStockIngredients();
    }

    @Transactional
    public void processStockMovement(Long ingredientId, BigDecimal quantity,
                                     StockMovement.MovementType movementType,
                                     StockMovement.MovementReason reason, String notes) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        StockMovement movement = new StockMovement();
        movement.setIngredient(ingredient);
        movement.setQuantity(quantity);
        movement.setMovementType(movementType);
        movement.setReason(reason);
        movement.setNotes(notes);
        movement.setMovementDate(LocalDateTime.now());

        stockMovementRepository.save(movement);
    }

    public boolean canProduceDish(Long dishId, Integer portions) {
        // TODO: Реализовать логику проверки возможности приготовления блюда
        // Пока возвращаем true для тестирования
        return true;
    }

    public Map<String, Object> calculateRequiredIngredients(List<Long> orderItemIds) {
        // TODO: Реализовать логику расчета необходимых ингредиентов
        // Пока возвращаем пустой Map для тестирования
        return Map.of("message", "Feature not implemented yet");
    }

    public List<InventoryDTO.ConsumptionStats> getConsumptionStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> consumptionData = stockMovementRepository.getIngredientConsumptionBetweenDates(startDate, endDate);

        return consumptionData.stream()
                .map(obj -> new InventoryDTO.ConsumptionStats(
                        ((Ingredient) obj[0]).getIngredientName(),
                        (BigDecimal) obj[1],
                        ((Ingredient) obj[0]).getUnitOfMeasure()
                ))
                .collect(Collectors.toList());
    }
}