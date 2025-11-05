package com.restaurant.controller;

import com.restaurant.dto.CommonDTO;
import com.restaurant.dto.OrderDTO;
import com.restaurant.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasRole('WAITER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<OrderDTO.OrderResponse>> createOrder(
            @Valid @RequestBody OrderDTO.CreateOrderRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            OrderDTO.OrderResponse response = orderService.createOrder(request, userDetails.getUsername());
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Order created successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('WAITER') or hasRole('MANAGER') or hasRole('ADMIN') or hasRole('CHEF')")
    public ResponseEntity<CommonDTO.ApiResponse<OrderDTO.OrderResponse>> getOrderById(@PathVariable Long id) {
        try {
            OrderDTO.OrderResponse order = orderService.getOrderById(id);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(order));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/number/{orderNumber}")
    @PreAuthorize("hasRole('WAITER') or hasRole('MANAGER') or hasRole('ADMIN') or hasRole('CHEF')")
    public ResponseEntity<CommonDTO.ApiResponse<OrderDTO.OrderResponse>> getOrderByNumber(
            @PathVariable String orderNumber) {
        try {
            OrderDTO.OrderResponse order = orderService.getOrderByNumber(orderNumber);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(order));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('WAITER') or hasRole('MANAGER') or hasRole('ADMIN') or hasRole('CHEF')")
    public ResponseEntity<CommonDTO.ApiResponse<List<OrderDTO.OrderResponse>>> getOrdersByStatus(
            @PathVariable String status) {
        List<OrderDTO.OrderResponse> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(orders));
    }

    @GetMapping("/table/{tableNumber}")
    @PreAuthorize("hasRole('WAITER') or hasRole('MANAGER') or hasRole('ADMIN') or hasRole('CHEF')")
    public ResponseEntity<CommonDTO.ApiResponse<List<OrderDTO.OrderResponse>>> getOrdersByTable(
            @PathVariable String tableNumber) {
        List<OrderDTO.OrderResponse> orders = orderService.getOrdersByTable(tableNumber);
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(orders));
    }

    @GetMapping("/waiter/{waiterUsername}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<List<OrderDTO.OrderResponse>>> getOrdersByWaiter(
            @PathVariable String waiterUsername) {
        List<OrderDTO.OrderResponse> orders = orderService.getOrdersByWaiter(waiterUsername);
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(orders));
    }

    @PutMapping("/{id}/status/{status}")
    @PreAuthorize("hasRole('WAITER') or hasRole('MANAGER') or hasRole('ADMIN') or hasRole('CHEF')")
    public ResponseEntity<CommonDTO.ApiResponse<OrderDTO.OrderResponse>> updateOrderStatus(
            @PathVariable Long id,
            @PathVariable String status) {
        try {
            OrderDTO.OrderResponse response = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Order status updated", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/items")
    @PreAuthorize("hasRole('WAITER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<OrderDTO.OrderResponse>> addOrderItem(
            @PathVariable Long id,
            @Valid @RequestBody OrderDTO.OrderItemRequest itemRequest) {
        try {
            OrderDTO.OrderResponse response = orderService.addOrderItem(id, itemRequest);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Item added to order", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<Map<String, Object>>> getOrderStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            Map<String, Object> statistics = orderService.getOrderStatistics(startDate, endDate);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(statistics));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    // Для повара - получить заказы для кухни
    @GetMapping("/kitchen")
    @PreAuthorize("hasRole('CHEF') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<List<OrderDTO.OrderResponse>>> getKitchenOrders() {
        List<OrderDTO.OrderResponse> orders = orderService.getOrdersByStatus("готовится");
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(orders));
    }

    // Для официанта - получить свои заказы
    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('WAITER')")
    public ResponseEntity<CommonDTO.ApiResponse<List<OrderDTO.OrderResponse>>> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<OrderDTO.OrderResponse> orders = orderService.getOrdersByWaiter(userDetails.getUsername());
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(orders));
    }
}