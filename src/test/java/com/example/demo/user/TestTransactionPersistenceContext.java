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
public class TestTransactionPersistenceContext {
	
	@Autowired
	TransactionPersistenceContextUserService tpcUserServie;
	
	@Autowired
	ExtendedPersistenceContextUserService epcUserService;
	
	@Test
	public void testUserInsertWithTransaction() {
		//Pesistence Context(X) DB(O)
		Member member  = new Member(121L,"mskwon","admin");
		tpcUserServie.insertWithTransaction(member);
		
		log.debug("[TPC]Is Persistence Context: {}",tpcUserServie.isPersistenceContext(member));
		Member userFromTransactionPersistenceContext = tpcUserServie.find(member.getId());
		assertNotNull(userFromTransactionPersistenceContext);
		
		log.debug("[EPC]Is Persistence Context: {}",epcUserService.isPersistenceContext(member));
		Member userFromExtendedPersistenceContext = epcUserService.find(member.getId());
		assertNotNull(userFromExtendedPersistenceContext);
	}
	


}
