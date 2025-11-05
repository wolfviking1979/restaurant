package com.restaurant.controller;

import com.restaurant.dto.CommonDTO;
import com.restaurant.dto.InventoryDTO;
import com.restaurant.service.InventoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('STOREKEEPER') or hasRole('MANAGER') or hasRole('ADMIN') or hasRole('CHEF')")
    public ResponseEntity<CommonDTO.ApiResponse<List<InventoryDTO.IngredientResponse>>> getLowStockIngredients() {
        List<InventoryDTO.IngredientResponse> lowStock = inventoryService.getLowStockIngredients()
                .stream()
                .map(InventoryDTO.IngredientResponse::new) // Преобразуем Ingredient в IngredientResponse
                .collect(Collectors.toList());
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(lowStock));
    }

    @PostMapping("/movement")
    @PreAuthorize("hasRole('STOREKEEPER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<String>> processStockMovement(
            @RequestBody InventoryDTO.StockMovementRequest request) {
        try {
            inventoryService.processStockMovement(
                    request.getIngredientId(),
                    request.getQuantity(),
                    request.getMovementType(),
                    request.getReason(),
                    request.getNotes()
            );
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Stock movement processed successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/can-produce/{dishId}")
    @PreAuthorize("hasRole('CHEF') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<Boolean>> canProduceDish(
            @PathVariable Long dishId,
            @RequestParam Integer portions) {
        try {
            boolean canProduce = inventoryService.canProduceDish(dishId, portions);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(canProduce));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/required-ingredients")
    @PreAuthorize("hasRole('CHEF') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<Map<String, Object>>> calculateRequiredIngredients(
            @RequestParam List<Long> orderItemIds) {
        try {
            Map<String, Object> requiredIngredients = inventoryService.calculateRequiredIngredients(orderItemIds);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(requiredIngredients));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/statistics/consumption")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CommonDTO.ApiResponse<List<InventoryDTO.ConsumptionStats>>> getConsumptionStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<InventoryDTO.ConsumptionStats> statistics = inventoryService.getConsumptionStatistics(startDate, endDate);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(statistics));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }
}