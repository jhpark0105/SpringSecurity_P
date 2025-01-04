package pack;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {
	private Key key;
	
	@PostConstruct //생성자가 수행되고난다음 초기화
	public void init() {
		key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //암호화 알고리즘, 가장 일반적인 키가 HS256이다
	}
	
	public String createToken(String id) {
		return Jwts.builder()
				.setSubject(id)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))
				.signWith(key)
				.compact();
	}
	
	//주어진 토큰에서 사용자 id를 추출하기 위함
	public String getUseridFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
}
