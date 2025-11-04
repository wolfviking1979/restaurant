package com.restaurant.DTO;

import java.util.List;

public class CommonDTO {

    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;

        // Конструкторы
        public ApiResponse() {}

        public ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        // Статические фабричные методы
        public static <T> ApiResponse<T> success(T data) {
            return new ApiResponse<>(true, "Success", data);
        }

        public static <T> ApiResponse<T> success(String message, T data) {
            return new ApiResponse<>(true, message, data);
        }

        public static <T> ApiResponse<T> error(String message) {
            return new ApiResponse<>(false, message, null);
        }

        // ДОБАВЛЕН МЕТОД ДЛЯ ОШИБОК С ДАННЫМИ
        public static <T> ApiResponse<T> error(String message, T data) {
            return new ApiResponse<>(false, message, data);
        }

        // Геттеры и сеттеры
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

    public static class PageResponse<T> {
        private List<T> content;
        private int currentPage;
        private int totalPages;
        private long totalElements;

        public PageResponse() {}

        public PageResponse(List<T> content, int currentPage, int totalPages, long totalElements) {
            this.content = content;
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.totalElements = totalElements;
        }

        // Геттеры и сеттеры
        public List<T> getContent() { return content; }
        public void setContent(List<T> content) { this.content = content; }
        public int getCurrentPage() { return currentPage; }
        public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
        public long getTotalElements() { return totalElements; }
        public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    }
}