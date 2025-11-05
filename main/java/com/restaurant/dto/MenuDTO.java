package com.restaurant.dto;

import com.restaurant.entity.Dish;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MenuDTO {

    // Request DTO для блюда
    public static class CreateDishRequest {
        @NotBlank(message = "Dish name is required")
        private String dishName;

        private String description;

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        private BigDecimal price;

        private Integer weightGrams;
        private String imageUrl;

        @NotNull(message = "Category ID is required")
        private Long categoryId;

        private String composition;
        private String allergens;
        private Boolean isOnPromotion = false;
        private BigDecimal promotionPrice;

        // Геттеры и сеттеры
        public String getDishName() { return dishName; }
        public void setDishName(String dishName) { this.dishName = dishName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public Integer getWeightGrams() { return weightGrams; }
        public void setWeightGrams(Integer weightGrams) { this.weightGrams = weightGrams; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
        public String getComposition() { return composition; }
        public void setComposition(String composition) { this.composition = composition; }
        public String getAllergens() { return allergens; }
        public void setAllergens(String allergens) { this.allergens = allergens; }
        public Boolean getIsOnPromotion() { return isOnPromotion; }
        public void setIsOnPromotion(Boolean isOnPromotion) { this.isOnPromotion = isOnPromotion; }
        public BigDecimal getPromotionPrice() { return promotionPrice; }
        public void setPromotionPrice(BigDecimal promotionPrice) { this.promotionPrice = promotionPrice; }
    }

    // Response DTO для блюда
    public static class DishResponse {
        private Long id;
        private String dishName;
        private String description;
        private BigDecimal price;
        private Integer weightGrams;
        private String imageUrl;
        private String categoryName;
        private Boolean isActive;
        private Boolean isOnPromotion;
        private BigDecimal promotionPrice;
        private BigDecimal currentPrice;
        private String composition;
        private String allergens;
        private LocalDateTime createdAt;

        public DishResponse(Dish dish) {
            this.id = dish.getId();
            this.dishName = dish.getDishName();
            this.description = dish.getDescription();
            this.price = dish.getPrice();
            this.weightGrams = dish.getWeightGrams();
            this.imageUrl = dish.getImageUrl();
            this.categoryName = dish.getCategory().getCategoryName();
            this.isActive = dish.getIsActive();
            this.isOnPromotion = dish.getIsOnPromotion();
            this.promotionPrice = dish.getPromotionPrice();
            this.currentPrice = dish.getCurrentPrice();
            this.composition = dish.getComposition();
            this.allergens = dish.getAllergens();
            this.createdAt = dish.getCreatedAt();
        }

        // Геттеры и сеттеры
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getDishName() { return dishName; }
        public void setDishName(String dishName) { this.dishName = dishName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public Integer getWeightGrams() { return weightGrams; }
        public void setWeightGrams(Integer weightGrams) { this.weightGrams = weightGrams; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
        public Boolean getIsActive() { return isActive; }
        public void setIsActive(Boolean isActive) { this.isActive = isActive; }
        public Boolean getIsOnPromotion() { return isOnPromotion; }
        public void setIsOnPromotion(Boolean isOnPromotion) { this.isOnPromotion = isOnPromotion; }
        public BigDecimal getPromotionPrice() { return promotionPrice; }
        public void setPromotionPrice(BigDecimal promotionPrice) { this.promotionPrice = promotionPrice; }
        public BigDecimal getCurrentPrice() { return currentPrice; }
        public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
        public String getComposition() { return composition; }
        public void setComposition(String composition) { this.composition = composition; }
        public String getAllergens() { return allergens; }
        public void setAllergens(String allergens) { this.allergens = allergens; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
}