package com.restaurant.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(length = 500)
    private String notes;

    // Конструкторы
    public OrderItem() {}

    public OrderItem(Dish dish, Integer quantity) {
        this.dish = dish;
        this.quantity = quantity;
        this.unitPrice = dish.getCurrentPrice();
    }

    public OrderItem(Order order, Dish dish, Integer quantity, String notes) {
        this.order = order;
        this.dish = dish;
        this.quantity = quantity;
        this.unitPrice = dish.getCurrentPrice();
        this.notes = notes;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
        if (dish != null && this.unitPrice == null) {
            this.unitPrice = dish.getCurrentPrice();
        }
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Бизнес-методы
    public BigDecimal getItemTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public void updateQuantity(Integer newQuantity) {
        this.quantity = newQuantity;
        if (this.order != null) {
            this.order.recalculateTotalAmount();
        }
    }

    @PrePersist
    @PreUpdate
    public void validateUnitPrice() {
        if (this.unitPrice == null && this.dish != null) {
            this.unitPrice = this.dish.getCurrentPrice();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem)) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) &&
                Objects.equals(order, orderItem.order) &&
                Objects.equals(dish, orderItem.dish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, dish);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", dish=" + (dish != null ? dish.getDishName() : "null") +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", notes='" + notes + '\'' +
                '}';
    }
}