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
public class TestExtendedPersistenceContext {
	@Autowired
	TransactionPersistenceContextUserService tpcUserService;
	
	@Autowired
	ExtendedPersistenceContextUserService epcUserService;
	
	@Test
	public void testUserInsertWithTransaction() {
		
		Member member  = new Member(121L,"mskwon","admin");
		epcUserService.insertWithoutTransaction(member);
		log.debug("[EPC]Is Persistence Context: {}",epcUserService.isPersistenceContext(member));
		Member userFromExtendedPersistenceContext = epcUserService.find(member.getId());
		assertNotNull(userFromExtendedPersistenceContext);
		
		
		log.debug("[TPC]Is Persistence Context: {}",tpcUserService.isPersistenceContext(member));
		//Create New Persistence Context, Select In DB
		Member userFromTransactionPersistenceContext = tpcUserService.find(member.getId());
		assertNotNull(userFromTransactionPersistenceContext);
		

	}
	
}
