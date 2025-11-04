package com.restaurant.controller;

import com.restaurant.DTO.CommonDTO;
import com.restaurant.DTO.MenuDTO;
import com.restaurant.entity.MenuCategory;
import com.restaurant.repository.MenuCategoryRepository;
import com.restaurant.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@Validated
public class MenuController {

    private final MenuService menuService;
    private final MenuCategoryRepository categoryRepository;

    public MenuController(MenuService menuService, MenuCategoryRepository categoryRepository) {
        this.menuService = menuService;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/dishes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CommonDTO.ApiResponse<MenuDTO.DishResponse>> createDish(
            @Valid @RequestBody MenuDTO.CreateDishRequest request) {
        try {
            MenuDTO.DishResponse dishResponse = menuService.createDish(request);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Dish created successfully", dishResponse));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/dishes")
    public ResponseEntity<CommonDTO.ApiResponse<List<MenuDTO.DishResponse>>> getAllDishes() {
        List<MenuDTO.DishResponse> dishes = menuService.getAllDishes();
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(dishes));
    }

    @GetMapping("/dishes/active")
    public ResponseEntity<CommonDTO.ApiResponse<List<MenuDTO.DishResponse>>> getActiveDishes() {
        List<MenuDTO.DishResponse> dishes = menuService.getActiveDishes();
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(dishes));
    }

    @GetMapping("/dishes/category/{categoryId}")
    public ResponseEntity<CommonDTO.ApiResponse<List<MenuDTO.DishResponse>>> getDishesByCategory(
            @PathVariable Long categoryId) {
        try {
            List<MenuDTO.DishResponse> dishes = menuService.getDishesByCategory(categoryId);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(dishes));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/dishes/promotional")
    public ResponseEntity<CommonDTO.ApiResponse<List<MenuDTO.DishResponse>>> getPromotionalDishes() {
        List<MenuDTO.DishResponse> dishes = menuService.getPromotionalDishes();
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(dishes));
    }

    @GetMapping("/dishes/{id}")
    public ResponseEntity<CommonDTO.ApiResponse<MenuDTO.DishResponse>> getDishById(@PathVariable Long id) {
        try {
            MenuDTO.DishResponse dish = menuService.getDishById(id);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success(dish));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dishes/search")
    public ResponseEntity<CommonDTO.ApiResponse<List<MenuDTO.DishResponse>>> searchDishes(
            @RequestParam String query) {
        List<MenuDTO.DishResponse> dishes = menuService.searchDishes(query);
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(dishes));
    }

    @PutMapping("/dishes/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CommonDTO.ApiResponse<MenuDTO.DishResponse>> updateDish(
            @PathVariable Long id,
            @Valid @RequestBody MenuDTO.CreateDishRequest request) {
        try {
            MenuDTO.DishResponse dishResponse = menuService.updateDish(id, request);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Dish updated successfully", dishResponse));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/dishes/{id}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CommonDTO.ApiResponse<Void>> activateDish(@PathVariable Long id) {
        try {
            menuService.activateDish(id);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Dish activated successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/dishes/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CommonDTO.ApiResponse<Void>> deactivateDish(@PathVariable Long id) {
        try {
            menuService.deactivateDish(id);
            return ResponseEntity.ok(CommonDTO.ApiResponse.success("Dish deactivated successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<CommonDTO.ApiResponse<List<MenuCategory>>> getAllCategories() {
        List<MenuCategory> categories = categoryRepository.findAllByOrderByDisplayOrderAsc();
        return ResponseEntity.ok(CommonDTO.ApiResponse.success(categories));
    }
}