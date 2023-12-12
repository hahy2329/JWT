package com.application.jwt.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.jwt.service.SecurityService;

@RestController
@RequestMapping("/security")
public class SecurityController {
	
	@Autowired
	private SecurityService securityService;
	
	@GetMapping("/get/token") //서버측으로부터 토큰 발급
	public Map<String, Object> getToken(@RequestParam("subject") String subject) throws Exception{
		String token = securityService.CreateToken(subject,(2*1000*60));
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("result", token);
		return map;
		
	}
	
	@GetMapping("/get/subject") //발급 받은 키로 부터 서버의 키와 대조하여 같으면 Query String에 입력한 값을 반환
	public Map<String, Object> getSubject(@RequestParam("token") String token) throws Exception{
		String subject = securityService.getSubject(token);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("result", subject);
		return map;
	}
	
	//즉 처음에 클라이언트가 서버로 부터 로그인 요청을 시작으로하여 서버로부터 확인이 되면 토큰을 발급해준다. 그 이후부터 토큰을 기반으로 대조하여 맞다면 다음 요청할 때도 이에 맞는 데이터들을 받아보는 기법이다.
	
}
