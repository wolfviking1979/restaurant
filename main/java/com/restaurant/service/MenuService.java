package com.restaurant.service;

import com.restaurant.dto.MenuDTO;
import com.restaurant.entity.Dish;
import com.restaurant.entity.MenuCategory;
import com.restaurant.repository.DishRepository;
import com.restaurant.repository.MenuCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {

    private final DishRepository dishRepository;
    private final MenuCategoryRepository categoryRepository;

    public MenuService(DishRepository dishRepository, MenuCategoryRepository categoryRepository) {
        this.dishRepository = dishRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public MenuDTO.DishResponse createDish(MenuDTO.CreateDishRequest request) {
        MenuCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Dish dish = new Dish();
        dish.setDishName(request.getDishName());
        dish.setDescription(request.getDescription());
        dish.setPrice(request.getPrice());
        dish.setWeightGrams(request.getWeightGrams());
        dish.setImageUrl(request.getImageUrl());
        dish.setCategory(category);
        dish.setComposition(request.getComposition());
        dish.setAllergens(request.getAllergens());
        dish.setIsOnPromotion(request.getIsOnPromotion());
        dish.setPromotionPrice(request.getPromotionPrice());
        dish.setIsActive(true);

        Dish savedDish = dishRepository.save(dish);
        return new MenuDTO.DishResponse(savedDish);
    }

    public List<MenuDTO.DishResponse> getAllDishes() {
        return dishRepository.findAll().stream()
                .map(MenuDTO.DishResponse::new)
                .collect(Collectors.toList());
    }

    public List<MenuDTO.DishResponse> getActiveDishes() {
        return dishRepository.findByIsActiveTrue().stream()
                .map(MenuDTO.DishResponse::new)
                .collect(Collectors.toList());
    }

    public List<MenuDTO.DishResponse> getDishesByCategory(Long categoryId) {
        MenuCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return dishRepository.findByCategory(category).stream()
                .map(MenuDTO.DishResponse::new)
                .collect(Collectors.toList());
    }

    public List<MenuDTO.DishResponse> getPromotionalDishes() {
        return dishRepository.findByIsOnPromotionTrueAndIsActiveTrue().stream()
                .map(MenuDTO.DishResponse::new)
                .collect(Collectors.toList());
    }

    public MenuDTO.DishResponse getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found with id: " + id));
        return new MenuDTO.DishResponse(dish);
    }

    @Transactional
    public MenuDTO.DishResponse updateDish(Long id, MenuDTO.CreateDishRequest request) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found with id: " + id));

        MenuCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        dish.setDishName(request.getDishName());
        dish.setDescription(request.getDescription());
        dish.setPrice(request.getPrice());
        dish.setWeightGrams(request.getWeightGrams());
        dish.setImageUrl(request.getImageUrl());
        dish.setCategory(category);
        dish.setComposition(request.getComposition());
        dish.setAllergens(request.getAllergens());
        dish.setIsOnPromotion(request.getIsOnPromotion());
        dish.setPromotionPrice(request.getPromotionPrice());

        Dish updatedDish = dishRepository.save(dish);
        return new MenuDTO.DishResponse(updatedDish);
    }

    @Transactional
    public void activateDish(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found with id: " + id));
        dish.setIsActive(true);
        dishRepository.save(dish);
    }

    @Transactional
    public void deactivateDish(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found with id: " + id));
        dish.setIsActive(false);
        dishRepository.save(dish);
    }

    public List<MenuDTO.DishResponse> searchDishes(String searchTerm) {
        return dishRepository.findByDishNameContainingIgnoreCaseAndIsActiveTrue(searchTerm).stream()
                .map(MenuDTO.DishResponse::new)
                .collect(Collectors.toList());
    }
}