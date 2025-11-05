package com.restaurant.service;

import com.restaurant.dto.OrderDTO;
import com.restaurant.entity.*;
import com.restaurant.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RestaurantTableRepository tableRepository;
    private final ReservationRepository reservationRepository;
    private final DishRepository dishRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        RestaurantTableRepository tableRepository,
                        ReservationRepository reservationRepository,
                        DishRepository dishRepository,
                        OrderStatusRepository orderStatusRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
        this.dishRepository = dishRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderDTO.OrderResponse createOrder(OrderDTO.CreateOrderRequest request, String waiterUsername) {
        RestaurantTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found"));

        Reservation reservation = null;
        if (request.getReservationId() != null) {
            reservation = reservationRepository.findById(request.getReservationId())
                    .orElseThrow(() -> new RuntimeException("Reservation not found"));
        }

        User waiter = userRepository.findByUsername(waiterUsername)
                .orElseThrow(() -> new RuntimeException("Waiter not found"));

        OrderStatus initialStatus = orderStatusRepository.findByStatusName("принят")
                .orElseThrow(() -> new RuntimeException("Order status not found"));

        Order order = new Order();
        order.setTable(table);
        order.setReservation(reservation);
        order.setWaiter(waiter);
        order.setStatus(initialStatus);
        order.setNotes(request.getNotes());

        // Добавление элементов заказа
        for (OrderDTO.OrderItemRequest itemRequest : request.getOrderItems()) {
            Dish dish = dishRepository.findById(itemRequest.getDishId())
                    .orElseThrow(() -> new RuntimeException("Dish not found with id: " + itemRequest.getDishId()));

            if (!dish.getIsActive()) {
                throw new RuntimeException("Dish is not available: " + dish.getDishName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setDish(dish);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setNotes(itemRequest.getNotes());
            orderItem.setUnitPrice(dish.getCurrentPrice());

            order.addOrderItem(orderItem);
        }

        order.recalculateTotalAmount();
        Order savedOrder = orderRepository.save(order);
        return new OrderDTO.OrderResponse(savedOrder);
    }

    @Transactional
    public OrderDTO.OrderResponse updateOrderStatus(Long orderId, String statusName) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus newStatus = orderStatusRepository.findByStatusName(statusName)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        return new OrderDTO.OrderResponse(updatedOrder);
    }

    @Transactional
    public OrderDTO.OrderResponse addOrderItem(Long orderId, OrderDTO.OrderItemRequest itemRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.canBeModified()) {
            throw new RuntimeException("Cannot modify paid order");
        }

        Dish dish = dishRepository.findById(itemRequest.getDishId())
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        if (!dish.getIsActive()) {
            throw new RuntimeException("Dish is not available");
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setDish(dish);
        orderItem.setQuantity(itemRequest.getQuantity());
        orderItem.setNotes(itemRequest.getNotes());
        orderItem.setUnitPrice(dish.getCurrentPrice());

        order.addOrderItem(orderItem);
        Order updatedOrder = orderRepository.save(order);
        return new OrderDTO.OrderResponse(updatedOrder);
    }

    public List<OrderDTO.OrderResponse> getOrdersByStatus(String statusName) {
        return orderRepository.findByStatusName(statusName).stream()
                .map(OrderDTO.OrderResponse::new)
                .collect(Collectors.toList());
    }

    public List<OrderDTO.OrderResponse> getOrdersByTable(String tableNumber) {
        return orderRepository.findByTableNumber(tableNumber).stream()
                .map(OrderDTO.OrderResponse::new)
                .collect(Collectors.toList());
    }

    public List<OrderDTO.OrderResponse> getOrdersByWaiter(String waiterUsername) {
        User waiter = userRepository.findByUsername(waiterUsername)
                .orElseThrow(() -> new RuntimeException("Waiter not found"));

        return orderRepository.findByWaiter(waiter).stream()
                .map(OrderDTO.OrderResponse::new)
                .collect(Collectors.toList());
    }

    public OrderDTO.OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return new OrderDTO.OrderResponse(order);
    }

    public OrderDTO.OrderResponse getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return new OrderDTO.OrderResponse(order);
    }

    public Map<String, Object> getOrderStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        Long totalOrders = orderRepository.countPaidOrdersBetween(startDate, endDate);
        List<Object[]> popularDishes = orderItemRepository.findPopularDishesBetweenDates(startDate, endDate);

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalOrders", totalOrders);
        statistics.put("popularDishes", popularDishes.stream()
                .limit(10)
                .collect(Collectors.toList()));
        statistics.put("period", Map.of("start", startDate, "end", endDate));

        return statistics;
    }
}