package com.example.demo.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.user.model.Member;
import com.example.demo.user.service.ExtendedPersistenceContextUserService;
import com.example.demo.user.service.TransactionPersistenceContextUserService;

@SpringBootTest
public class TestExtendedPersistenceContext {
	@Autowired
	TransactionPersistenceContextUserService tpcUserServie;
	
	@Autowired
	ExtendedPersistenceContextUserService epcUserService;
	
	@Test
	public void testUserInsertWithTransaction() {
		
		Member member  = new Member(121L,"mskwon","admin");
		epcUserService.insertWithoutTransaction(member);
		//Select In Persistence Context
		Member userFromExtendedPersistenceContext = epcUserService.find(member.getId());
		assertNotNull(userFromExtendedPersistenceContext);
		
		//Create New Persistence Context, Select In DB
		Member userFromTransactionPersistenceContext = tpcUserServie.find(member.getId());
		assertNotNull(userFromTransactionPersistenceContext);
		

	}
	
	@Test
	public void testUserInsertWithTransaction2() {
		Member member1 = new Member(124L, "Devender", "admin");
		epcUserService.insertWithoutTransaction(member1);

		Member member2 = new Member(125L, "Devender", "admin");
		epcUserService.insertWithTransaction(member2);

		Member member1FromTransctionPersistenceContext = tpcUserServie
		  .find(member1.getId());
		assertNotNull(member1FromTransctionPersistenceContext);

		Member member2FromTransctionPersistenceContext = tpcUserServie
		  .find(member2.getId());
		assertNotNull(member2FromTransctionPersistenceContext);
		

	}
}
