package com.example.demo.user.service;

import org.springframework.stereotype.Component;

import com.example.demo.user.model.Member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class TransactionPersistenceContextUserService implements UserService{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@Transactional
	public Member insertWithTransaction(Member user) {
		entityManager.persist(user);
		return user;
	}
	
	@Override
	public Member insertWithoutTransaction(Member user) {
		entityManager.merge(user);
		entityManager.persist(user);
		return user;
	}
	
	@Override
	public Member find(Long id) {
		return entityManager.find(Member.class, id);
	}
	
	
}
