package com.restaurant.repository;

import com.restaurant.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    Optional<MenuCategory> findByCategoryName(String categoryName);
    List<MenuCategory> findByDisplayOrderGreaterThanOrderByDisplayOrder(Integer displayOrder);
    List<MenuCategory> findAllByOrderByDisplayOrderAsc();
}