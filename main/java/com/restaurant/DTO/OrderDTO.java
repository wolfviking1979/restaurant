package com.restaurant.DTO;

import com.restaurant.entity.Order;
import com.restaurant.entity.OrderItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {

    // DTO для элемента заказа
    public static class OrderItemRequest {
        @NotNull(message = "Dish ID is required")
        private Long dishId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        private String notes;

        // Геттеры и сеттеры
        public Long getDishId() { return dishId; }
        public void setDishId(Long dishId) { this.dishId = dishId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    // Request DTO для создания заказа
    public static class CreateOrderRequest {
        private Long reservationId;

        @NotNull(message = "Table ID is required")
        private Long tableId;

        private String notes;

        @NotEmpty(message = "Order items cannot be empty")
        private List<OrderItemRequest> orderItems;

        // Геттеры и сеттеры
        public Long getReservationId() { return reservationId; }
        public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
        public Long getTableId() { return tableId; }
        public void setTableId(Long tableId) { this.tableId = tableId; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public List<OrderItemRequest> getOrderItems() { return orderItems; }
        public void setOrderItems(List<OrderItemRequest> orderItems) { this.orderItems = orderItems; }
    }

    // Response DTO для элемента заказа
    public static class OrderItemResponse {
        private Long id;
        private String dishName;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal itemTotal;
        private String notes;

        public OrderItemResponse(OrderItem orderItem) {
            this.id = orderItem.getId();
            this.dishName = orderItem.getDish().getDishName();
            this.unitPrice = orderItem.getUnitPrice();
            this.quantity = orderItem.getQuantity();
            this.itemTotal = orderItem.getItemTotal();
            this.notes = orderItem.getNotes();
        }

        // Геттеры и сеттеры
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getDishName() { return dishName; }
        public void setDishName(String dishName) { this.dishName = dishName; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getItemTotal() { return itemTotal; }
        public void setItemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    // Response DTO для заказа
    public static class OrderResponse {
        private Long id;
        private String orderNumber;
        private String tableNumber;
        private String waiterName;
        private String status;
        private BigDecimal totalAmount;
        private String notes;
        private List<OrderItemResponse> orderItems;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public OrderResponse(Order order) {
            this.id = order.getId();
            this.orderNumber = order.getOrderNumber();
            this.tableNumber = order.getTable().getTableNumber();
            this.waiterName = order.getWaiter() != null ? order.getWaiter().getFullName() : null;
            this.status = order.getStatus().getStatusName();
            this.totalAmount = order.getTotalAmount();
            this.notes = order.getNotes();
            this.orderItems = order.getOrderItems().stream()
                    .map(OrderItemResponse::new)
                    .collect(Collectors.toList());
            this.createdAt = order.getCreatedAt();
            this.updatedAt = order.getUpdatedAt();
        }

        // Геттеры и сеттеры
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getOrderNumber() { return orderNumber; }
        public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
        public String getTableNumber() { return tableNumber; }
        public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
        public String getWaiterName() { return waiterName; }
        public void setWaiterName(String waiterName) { this.waiterName = waiterName; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public List<OrderItemResponse> getOrderItems() { return orderItems; }
        public void setOrderItems(List<OrderItemResponse> orderItems) { this.orderItems = orderItems; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    }
}