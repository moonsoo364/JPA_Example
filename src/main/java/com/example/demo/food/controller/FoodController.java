package com.example.demo.food.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.food.dto.FoodDto;
import com.example.demo.food.service.FoodService;

@RestController
@RequestMapping("/food")
public class FoodController {
	private final FoodService foodService;

	public FoodController(FoodService foodService) {
		this.foodService = foodService;
	}
	
	@GetMapping("/{foodName}")
	public ResponseEntity<?> findOne(@PathVariable(name="foodName") String foodName){
		return foodService
				.findOne(foodName)
				.map(FoodDto::fromEntity)
				.map(ResponseEntity::ok)
	            .orElse(ResponseEntity.notFound().build());
	}
	
}
