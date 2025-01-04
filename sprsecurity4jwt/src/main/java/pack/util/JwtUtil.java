package pack.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

//JWT 생성하고 검증하는 클래스
@Component
public class JwtUtil {
	// 고정된 비밀 키 사용 (예제용)  최소 256비트 길이의 비밀 키
	private final String SECRET_KEY = "mySuperSecretKey12345678901234567890123456789012";
	private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간
	
	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	}
	
	public String generateToken(String sabun) {
		return Jwts.builder()
				.setSubject(sabun)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(getSigningKey())
				.compact();
	}
	
	public Claims extractClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
				
	}
	
	public String extractUsername(String token) {
		return extractClaims(token).getSubject(); 
	}
	
	//JWT 유효성 검증
	public boolean validateToken(String token) {
		try {
			Claims claims = extractClaims(token);
			return !claims.getExpiration().before(new Date()); //토큰의 만료시간이 현재시간 이전이면 true(만료되지않은경우)
		} catch (Exception e) {
			//예외 상황 : 토큰 서명이 잘못된 경우, JWT 구조가 올바르지 않은 경우, 기타...
			return false;
		}
	}
}
