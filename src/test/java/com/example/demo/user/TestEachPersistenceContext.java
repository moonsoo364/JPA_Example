package com.example.demo.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.user.model.Member;
import com.example.demo.user.service.ExtendedPersistenceContextUserService;
import com.example.demo.user.service.TransactionPersistenceContextUserService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class TestEachPersistenceContext {
	@Autowired
	TransactionPersistenceContextUserService tpcUserService;
	
	@Autowired
	ExtendedPersistenceContextUserService epcUserService;
		
	@Test
	public void testUserInsertWithTransaction2() {
		Member member1 = new Member(124L, "Devender", "admin");
		epcUserService.insertWithoutTransaction(member1);
		Member member2 = new Member(125L, "Devender", "admin");
		// member1,2 save in database
		log.debug("[Start]Save In DataBase");
		epcUserService.insertWithTransaction(member2);
		log.debug("[End]Save In DataBase");
		Member member1FromTransctionPersistenceContext = tpcUserService
		  .find(member1.getId());
		assertNotNull(member1FromTransctionPersistenceContext);

		Member member2FromTransctionPersistenceContext = tpcUserService
		  .find(member2.getId());
		assertNotNull(member2FromTransctionPersistenceContext);
		

	}
}
