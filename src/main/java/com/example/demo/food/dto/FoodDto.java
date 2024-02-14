package com.example.demo.food.dto;

import java.util.Set;

import com.example.demo.food.model.Food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodDto {
	
	private Food food;
	public static FoodDto fromEntity(Food food) {
		FoodDto dto = new FoodDto(food);
		return dto;
	}
}
