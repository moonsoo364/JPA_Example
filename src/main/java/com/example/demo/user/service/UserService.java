package com.example.demo.user.service;

import com.example.demo.user.model.Member;

public interface UserService {
	
	
	Member insertWithTransaction(Member user) ;
	
	Member insertWithoutTransaction(Member user) ;
	
	Member find(Long id) ;
	
	boolean isPersistenceContext(Member member);

}
