package com.application.jwt.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;





@Service
public class SecurityServiceImpl implements SecurityService {
	
	private static final String SECRET_KEY ="aasjjkjaskjdl1k2naskjkdakj34c8cafffccfdsfszzxc"; //문자열도 256비트로 맞출 것.(해시 알고리즘인 HS256을 도입하기 위함).
	
	
	@Override
	public String CreateToken(String subject, long ttlMillis) { //createToken 메서드는 서버에서 토큰을 만들어서 발행하는 역할
		
		if(ttlMillis <= 0) {
			throw new RuntimeException("Expiry time must be greater than Zero :["+ttlMillis+"]");
		}//ttlMillis가 0보다 작거나 같은 경우, 해당 문자열을 출력("만료시간은 0보다 커야 한다.") => 프로그램이 더 이상 동작하지 않게 만들기.
		
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		//토큰을 서명하기 위해 사용해야 할 알고리즘 종류 선택
		
		
		//서명에 담을 데이터
		byte[] secretKeyBytes =  DatatypeConverter.parseBase64Binary(SECRET_KEY); // secret key를 인코딩
		Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName()); // 비밀키 생성
		
		//헤더에 담을 데이터
		Map<String, Object> headerMap = new HashMap<String, Object>();
		
		headerMap.put("typ", "JWT");
		headerMap.put("alg", "HS256");
		
		//토큰 빌드
		JwtBuilder builder = Jwts.builder().setHeader(headerMap).setSubject(subject).signWith(signingKey, signatureAlgorithm).setExpiration(new Date(System.currentTimeMillis()+ttlMillis));
		
		
		return builder.compact(); //compact는 위 설정대로 JWT 토큰을 생성하겠다.
		
	}

	@Override
	public String getSubject(String token) { //getSubject 메서드는 비밀키로 토큰을 풀어서 값을 가져오는 역할
		
		Claims claims = Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)).build().parseClaimsJws(token).getBody();
		
		return claims.getSubject();
	}

}
