package com.restaurant.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "dish_recipes")
public class DishRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(name = "quantity_required", nullable = false, precision = 8, scale = 3)
    private BigDecimal quantityRequired;

    // Конструкторы
    public DishRecipe() {}

    public DishRecipe(Dish dish, Ingredient ingredient, BigDecimal quantityRequired) {
        this.dish = dish;
        this.ingredient = ingredient;
        this.quantityRequired = quantityRequired;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public BigDecimal getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(BigDecimal quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    // Бизнес-методы
    public BigDecimal getTotalCost() {
        return ingredient.getCostPerUnit() != null ?
                quantityRequired.multiply(ingredient.getCostPerUnit()) : BigDecimal.ZERO;
    }

    public boolean canProduce(Integer portions) {
        BigDecimal requiredTotal = quantityRequired.multiply(BigDecimal.valueOf(portions));
        return ingredient.hasSufficientStock(requiredTotal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DishRecipe)) return false;
        DishRecipe that = (DishRecipe) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(dish, that.dish) &&
                Objects.equals(ingredient, that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dish, ingredient);
    }

    @Override
    public String toString() {
        return "DishRecipe{" +
                "id=" + id +
                ", dish=" + (dish != null ? dish.getDishName() : "null") +
                ", ingredient=" + (ingredient != null ? ingredient.getIngredientName() : "null") +
                ", quantityRequired=" + quantityRequired +
                '}';
    }
}