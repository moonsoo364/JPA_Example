package com.example.demo.food.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.food.model.Food;
import com.example.demo.food.repository.FoodRepository;

@Service
public class FoodServiceImpl implements FoodService{
	
	private final FoodRepository foodRepository;
	
	public FoodServiceImpl (FoodRepository foodRepository) {
		this.foodRepository = foodRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Food> findOne(String foodName) {
		return foodRepository.findByFoodName(foodName);
	}

	@Override
	public Optional<Food> findOneNoTransaction(String foodName) {
		Optional<Food> food = foodRepository.findByFoodName(foodName);
		if(food.isPresent()) {
			
		}
		return food;
	}

}
