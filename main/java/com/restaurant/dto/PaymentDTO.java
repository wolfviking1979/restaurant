package com.restaurant.dto;

import com.restaurant.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class PaymentDTO {

    public static class CreatePaymentRequest {
        private Long orderId;
        private BigDecimal amount;
        private Payment.PaymentMethod paymentMethod;

        // Геттеры и сеттеры
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public Payment.PaymentMethod getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(Payment.PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    }

    public static class PaymentResponse {
        private Long id;
        private String orderNumber;
        private BigDecimal amount;
        private String paymentMethod;
        private String paymentStatus;
        private String transactionId;
        private LocalDateTime paidAt;
        private LocalDateTime createdAt;

        public PaymentResponse(Payment payment) {
            this.id = payment.getId();
            this.orderNumber = payment.getOrder().getOrderNumber();
            this.amount = payment.getAmount();
            this.paymentMethod = payment.getPaymentMethod().name();
            this.paymentStatus = payment.getPaymentStatus().name();
            this.transactionId = payment.getTransactionId();
            this.paidAt = payment.getPaidAt();
            this.createdAt = payment.getCreatedAt();
        }

        // Геттеры и сеттеры
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getOrderNumber() { return orderNumber; }
        public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        public String getPaymentStatus() { return paymentStatus; }
        public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
        public String getTransactionId() { return transactionId; }
        public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
        public LocalDateTime getPaidAt() { return paidAt; }
        public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    public static class RevenueStatistics {
        private BigDecimal totalRevenue;
        private Map<String, BigDecimal> revenueByPaymentMethod;
        private Long totalTransactions;
        private LocalDateTime startDate;
        private LocalDateTime endDate;

        // Конструкторы, геттеры и сеттеры
        public RevenueStatistics() {}

        public RevenueStatistics(BigDecimal totalRevenue, Map<String, BigDecimal> revenueByPaymentMethod,
                                 Long totalTransactions, LocalDateTime startDate, LocalDateTime endDate) {
            this.totalRevenue = totalRevenue;
            this.revenueByPaymentMethod = revenueByPaymentMethod;
            this.totalTransactions = totalTransactions;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        // Геттеры и сеттеры
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
        public Map<String, BigDecimal> getRevenueByPaymentMethod() { return revenueByPaymentMethod; }
        public void setRevenueByPaymentMethod(Map<String, BigDecimal> revenueByPaymentMethod) { this.revenueByPaymentMethod = revenueByPaymentMethod; }
        public Long getTotalTransactions() { return totalTransactions; }
        public void setTotalTransactions(Long totalTransactions) { this.totalTransactions = totalTransactions; }
        public LocalDateTime getStartDate() { return startDate; }
        public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
        public LocalDateTime getEndDate() { return endDate; }
        public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    }
}