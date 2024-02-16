package com.example.demo.food.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.food.model.Food;
import com.example.demo.food.repository.FoodRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FoodControllerTest {
	
	@Autowired
	private FoodRepository foodRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	 @BeforeEach
	    void setUp() {
	        Food food = new Food();
	        food.setFoodName("banana");
	        // 이 세션에서 NativeHibiatat Entity가 생성됨
	        food.setNativeHabitat(new HashSet<>(Arrays.asList("INDONESIA", "JAPAN")));
	        
	        foodRepository.save(food); 
	 	}
	 @Test
	 void giveFood() throws Exception {
		 // setUp() 매서드에서 생성된 세션을 사용, 이 테스트가 통과됨
		 mockMvc.perform(get("/food/banana"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.foodName").value("banana"))
         .andExpect(jsonPath("$.nativeHabitat", containsInAnyOrder("INDONESIA", "JAPAN")));
	 }

}
