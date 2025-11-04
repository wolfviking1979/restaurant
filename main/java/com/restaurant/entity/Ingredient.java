package com.restaurant.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ingredient_name", unique = true, nullable = false, length = 255)
    private String ingredientName;

    @Column(name = "unit_of_measure", nullable = false, length = 20)
    private String unitOfMeasure;

    @Column(name = "current_stock", precision = 10, scale = 3)
    private BigDecimal currentStock = BigDecimal.ZERO;

    @Column(name = "min_stock_level", precision = 10, scale = 3)
    private BigDecimal minStockLevel = BigDecimal.ZERO;

    @Column(name = "cost_per_unit", precision = 10, scale = 2)
    private BigDecimal costPerUnit;

    // Конструкторы
    public Ingredient() {}

    public Ingredient(String ingredientName, String unitOfMeasure) {
        this.ingredientName = ingredientName;
        this.unitOfMeasure = unitOfMeasure;
    }

    public Ingredient(String ingredientName, String unitOfMeasure,
                      BigDecimal minStockLevel, BigDecimal costPerUnit) {
        this.ingredientName = ingredientName;
        this.unitOfMeasure = unitOfMeasure;
        this.minStockLevel = minStockLevel;
        this.costPerUnit = costPerUnit;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public BigDecimal getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(BigDecimal currentStock) {
        this.currentStock = currentStock;
    }

    public BigDecimal getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(BigDecimal minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    public BigDecimal getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(BigDecimal costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    // Бизнес-методы
    public void addToStock(BigDecimal quantity) {
        this.currentStock = this.currentStock.add(quantity);
    }

    public void removeFromStock(BigDecimal quantity) {
        if (this.currentStock.compareTo(quantity) < 0) {
            throw new IllegalArgumentException("Недостаточно ингредиента на складе");
        }
        this.currentStock = this.currentStock.subtract(quantity);
    }

    public boolean isStockLow() {
        return currentStock.compareTo(minStockLevel) <= 0;
    }

    public BigDecimal getStockValue() {
        return costPerUnit != null ?
                currentStock.multiply(costPerUnit) : BigDecimal.ZERO;
    }

    public boolean hasSufficientStock(BigDecimal requiredQuantity) {
        return currentStock.compareTo(requiredQuantity) >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(ingredientName, that.ingredientName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredientName);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", ingredientName='" + ingredientName + '\'' +
                ", unitOfMeasure='" + unitOfMeasure + '\'' +
                ", currentStock=" + currentStock +
                ", minStockLevel=" + minStockLevel +
                ", costPerUnit=" + costPerUnit +
                '}';
    }
}