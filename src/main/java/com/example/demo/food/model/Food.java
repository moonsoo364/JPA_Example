package com.example.demo.food.model;

import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="food")
@Data
public class Food {

	@Id
	private String foodName;
	
	@ElementCollection
	private Set<String> nativeHabitat;
}
