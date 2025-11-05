package com.restaurant.service;

import com.restaurant.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    public ReportService(OrderRepository orderRepository,
                         OrderItemRepository orderItemRepository,
                         PaymentRepository paymentRepository,
                         ReservationRepository reservationRepository,
                         RestaurantTableRepository restaurantTableRepository,
                         UserRepository userRepository,
                         IngredientRepository ingredientRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.restaurantTableRepository = restaurantTableRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public Map<String, Object> getDailyRevenueReport(LocalDate date) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.atTime(23, 59, 59);

        BigDecimal totalRevenue = paymentRepository.getTotalRevenueBetween(startDateTime, endDateTime);
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;

        Long totalOrders = orderRepository.countPaidOrdersBetween(startDateTime, endDateTime);
        Long totalReservations = reservationRepository.countConfirmedReservationsByDate(date);

        Map<String, Object> report = new HashMap<>();
        report.put("date", date);
        report.put("totalRevenue", totalRevenue);
        report.put("totalOrders", totalOrders);
        report.put("totalReservations", totalReservations);
        report.put("averageOrderValue", totalOrders > 0 ?
                totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);

        return report;
    }

    public Map<String, Object> getRevenueReportByPeriod(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        BigDecimal totalRevenue = paymentRepository.getTotalRevenueBetween(startDateTime, endDateTime);
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;

        Long totalOrders = orderRepository.countPaidOrdersBetween(startDateTime, endDateTime);
        List<Object[]> revenueByMethod = paymentRepository.getRevenueByPaymentMethodBetween(startDateTime, endDateTime);

        Map<String, BigDecimal> revenueMap = revenueByMethod.stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> (BigDecimal) obj[1]
                ));

        Map<String, Object> report = new HashMap<>();
        report.put("period", Map.of("start", startDate, "end", endDate));
        report.put("totalRevenue", totalRevenue);
        report.put("totalOrders", totalOrders);
        report.put("revenueByPaymentMethod", revenueMap);
        report.put("averageOrderValue", totalOrders > 0 ?
                totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);

        return report;
    }

    public Map<String, Object> getPopularDishesReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Object[]> popularDishes = orderItemRepository.findPopularDishesBetweenDates(startDateTime, endDateTime);

        List<Map<String, Object>> popularDishesList = popularDishes.stream()
                .limit(10)
                .map(obj -> {
                    Map<String, Object> dishMap = new HashMap<>();
                    dishMap.put("dishName", ((com.restaurant.entity.Dish) obj[0]).getDishName());
                    dishMap.put("totalQuantity", obj[1]);
                    dishMap.put("category", ((com.restaurant.entity.Dish) obj[0]).getCategory().getCategoryName());
                    return dishMap;
                })
                .collect(Collectors.toList());

        Map<String, Object> report = new HashMap<>();
        report.put("period", Map.of("start", startDate, "end", endDate));
        report.put("popularDishes", popularDishesList);

        return report;
    }

    public Map<String, Object> getTableOccupancyReport(LocalDate startDate, LocalDate endDate) {
        List<Object[]> popularTables = reservationRepository.getPopularTables(startDate, endDate);
        Long totalTables = restaurantTableRepository.countActiveTables();

        List<Map<String, Object>> tableUtilization = popularTables.stream()
                .map(obj -> {
                    Map<String, Object> tableMap = new HashMap<>();
                    tableMap.put("tableNumber", ((com.restaurant.entity.RestaurantTable) obj[0]).getTableNumber());
                    tableMap.put("reservationCount", obj[1]);
                    tableMap.put("occupancyRate", totalTables > 0 ?
                            ((Long) obj[1]).doubleValue() / totalTables * 100 : 0);
                    return tableMap;
                })
                .collect(Collectors.toList());

        Map<String, Object> report = new HashMap<>();
        report.put("period", Map.of("start", startDate, "end", endDate));
        report.put("totalTables", totalTables);
        report.put("tableUtilization", tableUtilization);

        return report;
    }

    public Map<String, Object> getStaffEfficiencyReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        // Получаем всех официантов
        List<com.restaurant.entity.User> waiters = userRepository.findByRole_RoleName("WAITER");

        List<Map<String, Object>> waiterStats = waiters.stream()
                .map(waiter -> {
                    List<com.restaurant.entity.Order> waiterOrders =
                            orderRepository.findByWaiterAndPeriod(waiter.getId(), startDateTime, endDateTime);

                    Long totalOrders = (long) waiterOrders.size();
                    BigDecimal totalSales = waiterOrders.stream()
                            .map(com.restaurant.entity.Order::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal averageOrderValue = totalOrders > 0 ?
                            totalSales.divide(BigDecimal.valueOf(totalOrders), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

                    Map<String, Object> waiterMap = new HashMap<>();
                    waiterMap.put("waiterName", waiter.getFullName());
                    waiterMap.put("totalOrders", totalOrders);
                    waiterMap.put("totalSales", totalSales);
                    waiterMap.put("averageOrderValue", averageOrderValue);

                    return waiterMap;
                })
                .collect(Collectors.toList());

        Map<String, Object> report = new HashMap<>();
        Map<String, Object> periodMap = new HashMap<>();
        periodMap.put("start", startDate);
        periodMap.put("end", endDate);
        report.put("period", periodMap);
        report.put("waiterStatistics", waiterStats);

        return report;
    }

    public Map<String, Object> getInventoryReport() {
        List<com.restaurant.entity.Ingredient> lowStockIngredients = ingredientRepository.findLowStockIngredients();
        List<com.restaurant.entity.Ingredient> outOfStockIngredients = ingredientRepository.findOutOfStockIngredients();

        List<Map<String, Object>> lowStockList = lowStockIngredients.stream()
                .map(ing -> {
                    Map<String, Object> ingredientMap = new HashMap<>();
                    ingredientMap.put("name", ing.getIngredientName());
                    ingredientMap.put("currentStock", ing.getCurrentStock());
                    ingredientMap.put("minStock", ing.getMinStockLevel());
                    ingredientMap.put("unit", ing.getUnitOfMeasure());
                    return ingredientMap;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> outOfStockList = outOfStockIngredients.stream()
                .map(ing -> {
                    Map<String, Object> ingredientMap = new HashMap<>();
                    ingredientMap.put("name", ing.getIngredientName());
                    ingredientMap.put("unit", ing.getUnitOfMeasure());
                    return ingredientMap;
                })
                .collect(Collectors.toList());

        Map<String, Object> report = new HashMap<>();
        report.put("totalIngredients", ingredientRepository.count());
        report.put("lowStockCount", lowStockIngredients.size());
        report.put("outOfStockCount", outOfStockIngredients.size());
        report.put("lowStockIngredients", lowStockList);
        report.put("outOfStockIngredients", outOfStockList);

        return report;
    }
}