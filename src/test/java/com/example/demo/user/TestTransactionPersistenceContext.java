package com.example.demo.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.user.model.Member;
import com.example.demo.user.service.ExtendedPersistenceContextUserService;
import com.example.demo.user.service.TransactionPersistenceContextUserService;

@SpringBootTest
public class TestTransactionPersistenceContext {
	
	@Autowired
	TransactionPersistenceContextUserService tpcUserServie;
	
	@Autowired
	ExtendedPersistenceContextUserService epcUserService;
	
	@Test
	public void testUserInsertWithTransaction() {
		//Pesistence Context(O) DB(O)
		Member member  = new Member(121L,"mskwon","admin");
		tpcUserServie.insertWithTransaction(member);
		// Select In Persistence Context
		Member userFromTransactionPersistenceContext = tpcUserServie.find(member.getId());
		assertNotNull(userFromTransactionPersistenceContext);
		// Select In DB
		Member userFromExtendedPersistenceContext = epcUserService.find(member.getId());
		assertNotNull(userFromExtendedPersistenceContext);
	}
	


}
