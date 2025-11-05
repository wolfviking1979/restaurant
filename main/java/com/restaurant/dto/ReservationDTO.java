package com.restaurant.dto;

import com.restaurant.entity.Reservation;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationDTO {

    // Request DTO для создания бронирования
    public static class CreateReservationRequest {
        @NotBlank(message = "Guest name is required")
        private String guestName;

        private String guestPhone;

        @Email(message = "Email should be valid")
        private String guestEmail;

        @NotNull(message = "Reservation date is required")
        @Future(message = "Reservation date must be in the future")
        private LocalDate reservationDate;

        @NotNull(message = "Reservation time is required")
        private LocalTime reservationTime;

        @NotNull(message = "Guests count is required")
        @Min(value = 1, message = "Guests count must be at least 1")
        private Integer guestsCount;

        @NotNull(message = "Table ID is required")
        private Long tableId;

        private Integer durationMinutes = 120;
        private String specialRequests;

        // Геттеры и сеттеры
        public String getGuestName() { return guestName; }
        public void setGuestName(String guestName) { this.guestName = guestName; }
        public String getGuestPhone() { return guestPhone; }
        public void setGuestPhone(String guestPhone) { this.guestPhone = guestPhone; }
        public String getGuestEmail() { return guestEmail; }
        public void setGuestEmail(String guestEmail) { this.guestEmail = guestEmail; }
        public LocalDate getReservationDate() { return reservationDate; }
        public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }
        public LocalTime getReservationTime() { return reservationTime; }
        public void setReservationTime(LocalTime reservationTime) { this.reservationTime = reservationTime; }
        public Integer getGuestsCount() { return guestsCount; }
        public void setGuestsCount(Integer guestsCount) { this.guestsCount = guestsCount; }
        public Long getTableId() { return tableId; }
        public void setTableId(Long tableId) { this.tableId = tableId; }
        public Integer getDurationMinutes() { return durationMinutes; }
        public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
        public String getSpecialRequests() { return specialRequests; }
        public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    }

    // Response DTO для бронирования
    public static class ReservationResponse {
        private Long id;
        private String guestName;
        private String guestPhone;
        private String guestEmail;
        private LocalDate reservationDate;
        private LocalTime reservationTime;
        private Integer durationMinutes;
        private Integer guestsCount;
        private String tableNumber;
        private Integer tableCapacity;
        private String status;
        private String specialRequests;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ReservationResponse(Reservation reservation) {
            this.id = reservation.getId();
            this.guestName = reservation.getGuestName();
            this.guestPhone = reservation.getGuestPhone();
            this.guestEmail = reservation.getGuestEmail();
            this.reservationDate = reservation.getReservationDate();
            this.reservationTime = reservation.getReservationTime();
            this.durationMinutes = reservation.getDurationMinutes();
            this.guestsCount = reservation.getGuestsCount();
            this.tableNumber = reservation.getTable().getTableNumber();
            this.tableCapacity = reservation.getTable().getCapacity();
            this.status = reservation.getStatus().name();
            this.specialRequests = reservation.getSpecialRequests();
            this.createdAt = reservation.getCreatedAt();
            this.updatedAt = reservation.getUpdatedAt();
        }

        // Геттеры и сеттеры
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getGuestName() { return guestName; }
        public void setGuestName(String guestName) { this.guestName = guestName; }
        public String getGuestPhone() { return guestPhone; }
        public void setGuestPhone(String guestPhone) { this.guestPhone = guestPhone; }
        public String getGuestEmail() { return guestEmail; }
        public void setGuestEmail(String guestEmail) { this.guestEmail = guestEmail; }
        public LocalDate getReservationDate() { return reservationDate; }
        public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }
        public LocalTime getReservationTime() { return reservationTime; }
        public void setReservationTime(LocalTime reservationTime) { this.reservationTime = reservationTime; }
        public Integer getDurationMinutes() { return durationMinutes; }
        public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
        public Integer getGuestsCount() { return guestsCount; }
        public void setGuestsCount(Integer guestsCount) { this.guestsCount = guestsCount; }
        public String getTableNumber() { return tableNumber; }
        public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
        public Integer getTableCapacity() { return tableCapacity; }
        public void setTableCapacity(Integer tableCapacity) { this.tableCapacity = tableCapacity; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getSpecialRequests() { return specialRequests; }
        public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    }
}