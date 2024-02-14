package com.example.demo.food.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.food.model.Food;

public interface FoodRepository extends JpaRepository<Food, String>{

	Optional<Food> findByFoodName(String foodName);
}
