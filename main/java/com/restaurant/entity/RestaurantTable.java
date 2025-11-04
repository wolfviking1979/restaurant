package com.restaurant.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tables")
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_number", unique = true, nullable = false, length = 10)
    private String tableNumber;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "table_type")
    private TableType tableType = TableType.STANDARD;

    @Column(length = 500)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    public enum TableType {
        STANDARD, BAR, VIP, BANQUET
    }

    // Конструкторы
    public RestaurantTable() {}

    public RestaurantTable(String tableNumber, Integer capacity) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
    }

    public RestaurantTable(String tableNumber, Integer capacity, TableType tableType,
                           String description, Boolean isActive) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.tableType = tableType;
        this.description = description;
        this.isActive = isActive;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public TableType getTableType() {
        return tableType;
    }

    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantTable)) return false;
        RestaurantTable that = (RestaurantTable) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(tableNumber, that.tableNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableNumber);
    }

    // toString
    @Override
    public String toString() {
        return "RestaurantTable{" +
                "id=" + id +
                ", tableNumber='" + tableNumber + '\'' +
                ", capacity=" + capacity +
                ", tableType=" + tableType +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}