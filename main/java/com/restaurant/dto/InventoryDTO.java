package com.restaurant.dto;

import com.restaurant.entity.Ingredient;
import com.restaurant.entity.StockMovement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryDTO {

    public static class IngredientResponse {
        private Long id;
        private String ingredientName;
        private String unitOfMeasure;
        private BigDecimal currentStock;
        private BigDecimal minStockLevel;
        private BigDecimal costPerUnit;
        private Boolean isStockLow;

        public IngredientResponse(Ingredient ingredient) {
            this.id = ingredient.getId();
            this.ingredientName = ingredient.getIngredientName();
            this.unitOfMeasure = ingredient.getUnitOfMeasure();
            this.currentStock = ingredient.getCurrentStock();
            this.minStockLevel = ingredient.getMinStockLevel();
            this.costPerUnit = ingredient.getCostPerUnit();
            this.isStockLow = ingredient.isStockLow();
        }

        // Геттеры и сеттеры
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getIngredientName() { return ingredientName; }
        public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }
        public String getUnitOfMeasure() { return unitOfMeasure; }
        public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }
        public BigDecimal getCurrentStock() { return currentStock; }
        public void setCurrentStock(BigDecimal currentStock) { this.currentStock = currentStock; }
        public BigDecimal getMinStockLevel() { return minStockLevel; }
        public void setMinStockLevel(BigDecimal minStockLevel) { this.minStockLevel = minStockLevel; }
        public BigDecimal getCostPerUnit() { return costPerUnit; }
        public void setCostPerUnit(BigDecimal costPerUnit) { this.costPerUnit = costPerUnit; }
        public Boolean getIsStockLow() { return isStockLow; }
        public void setIsStockLow(Boolean isStockLow) { this.isStockLow = isStockLow; }
    }

    public static class StockMovementRequest {
        private Long ingredientId;
        private BigDecimal quantity;
        private StockMovement.MovementType movementType;
        private StockMovement.MovementReason reason;
        private String notes;
        private Long relatedOrderId;

        // Геттеры и сеттеры
        public Long getIngredientId() { return ingredientId; }
        public void setIngredientId(Long ingredientId) { this.ingredientId = ingredientId; }
        public BigDecimal getQuantity() { return quantity; }
        public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
        public StockMovement.MovementType getMovementType() { return movementType; }
        public void setMovementType(StockMovement.MovementType movementType) { this.movementType = movementType; }
        public StockMovement.MovementReason getReason() { return reason; }
        public void setReason(StockMovement.MovementReason reason) { this.reason = reason; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public Long getRelatedOrderId() { return relatedOrderId; }
        public void setRelatedOrderId(Long relatedOrderId) { this.relatedOrderId = relatedOrderId; }
    }

    public static class ConsumptionStats {
        private String ingredientName;
        private BigDecimal totalConsumed;
        private String unitOfMeasure;

        public ConsumptionStats(String ingredientName, BigDecimal totalConsumed, String unitOfMeasure) {
            this.ingredientName = ingredientName;
            this.totalConsumed = totalConsumed;
            this.unitOfMeasure = unitOfMeasure;
        }

        // Геттеры и сеттеры
        public String getIngredientName() { return ingredientName; }
        public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }
        public BigDecimal getTotalConsumed() { return totalConsumed; }
        public void setTotalConsumed(BigDecimal totalConsumed) { this.totalConsumed = totalConsumed; }
        public String getUnitOfMeasure() { return unitOfMeasure; }
        public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }
    }
}