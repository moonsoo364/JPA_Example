package com.example.demo.food.service;

import java.util.Optional;

import com.example.demo.food.model.Food;

public interface FoodService {
	
	Optional<Food> findOne(String foodName);
}
