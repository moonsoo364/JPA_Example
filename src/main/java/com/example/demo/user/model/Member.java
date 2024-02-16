package com.example.demo.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member {

	@Id
	private Long id;
	
	@Column
	private String userName;
	
	@Column
	private String userRole;
}
