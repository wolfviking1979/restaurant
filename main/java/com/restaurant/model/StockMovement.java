package com.restaurant.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 20)
    private MovementType movementType;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MovementReason reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_order_id")
    private Order relatedOrder;

    @Column(length = 1000)
    private String notes;

    @Column(name = "movement_date")
    private LocalDateTime movementDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by_user_id")
    private User performedBy;

    public enum MovementType {
        INCOME, OUTCOME
    }

    public enum MovementReason {
        PURCHASE, WRITE_OFF, ADJUSTMENT, PRODUCTION
    }

    // Конструкторы
    public StockMovement() {}

    public StockMovement(Ingredient ingredient, MovementType movementType,
                         BigDecimal quantity, MovementReason reason) {
        this.ingredient = ingredient;
        this.movementType = movementType;
        this.quantity = quantity;
        this.reason = reason;
    }

    public StockMovement(Ingredient ingredient, MovementType movementType,
                         BigDecimal quantity, MovementReason reason, User performedBy) {
        this.ingredient = ingredient;
        this.movementType = movementType;
        this.quantity = quantity;
        this.reason = reason;
        this.performedBy = performedBy;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public MovementReason getReason() {
        return reason;
    }

    public void setReason(MovementReason reason) {
        this.reason = reason;
    }

    public Order getRelatedOrder() {
        return relatedOrder;
    }

    public void setRelatedOrder(Order relatedOrder) {
        this.relatedOrder = relatedOrder;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(LocalDateTime movementDate) {
        this.movementDate = movementDate;
    }

    public User getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
    }

    // Бизнес-методы
    public boolean isIncome() {
        return movementType == MovementType.INCOME;
    }

    public boolean isOutcome() {
        return movementType == MovementType.OUTCOME;
    }

    public BigDecimal getSignedQuantity() {
        return isIncome() ? quantity : quantity.negate();
    }

    public void applyToIngredient() {
        if (isIncome()) {
            ingredient.addToStock(quantity);
        } else {
            ingredient.removeFromStock(quantity);
        }
    }

    @PrePersist
    public void prePersist() {
        if (movementDate == null) {
            movementDate = LocalDateTime.now();
        }
        applyToIngredient();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockMovement)) return false;
        StockMovement that = (StockMovement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", ingredient=" + (ingredient != null ? ingredient.getIngredientName() : "null") +
                ", movementType=" + movementType +
                ", quantity=" + quantity +
                ", reason=" + reason +
                ", movementDate=" + movementDate +
                '}';
    }
}