package com.application.jwt.service;


public interface SecurityService {
	
	public String CreateToken(String subject, long ttlMillis); 
	//토큰 생성
	public String getSubject(String token);
	//토큰 검증
}
