package com.restaurant.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "menu_categories")
public class MenuCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", unique = true, nullable = false, length = 100)
    private String categoryName;

    @Column(length = 500)
    private String description;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    // Конструкторы
    public MenuCategory() {}

    public MenuCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public MenuCategory(String categoryName, String description, Integer displayOrder) {
        this.categoryName = categoryName;
        this.description = description;
        this.displayOrder = displayOrder;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    // equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuCategory)) return false;
        MenuCategory that = (MenuCategory) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(categoryName, that.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryName);
    }

    // toString
    @Override
    public String toString() {
        return "MenuCategory{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", description='" + description + '\'' +
                ", displayOrder=" + displayOrder +
                '}';
    }
}