package com.example.demo.user.service;

import org.springframework.stereotype.Component;

import com.example.demo.user.model.Member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.transaction.Transactional;

@Component
public class ExtendedPersistenceContextUserService  implements UserService{
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;
	
	@Override
	@Transactional
	public Member insertWithTransaction(Member user) {
		entityManager.persist(user);
		return user;
	}
	
	@Override
	public Member insertWithoutTransaction(Member user) {
		entityManager.persist(user);
		return user;
	}
	
	@Override
	public Member find(Long id) {
		return entityManager.find(Member.class, id);
	}

}
