package pack;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pack.util.JwtUtil;

//Jwt 인증을 처리하는 필터, 요청이 들어올때마다 실행
//요청의 헤더 또는 쿠키에서 JWT 추출
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	// 동일한 요청 내에서 중복 호출 방지를 보장 (불필요한 작업을 방지)
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;
	
	public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//필터의 핵심로직을 작성하는 부분
		String token = null;
		
		String authHeader = request.getHeader("Authorization");
		System.out.println("authHeader : " + authHeader);
		
		// 토큰 얻는 방법 두가지 : 다양한 클라이언트 환경 및 요청에 대해 유연한 대처가 필요함
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7); //Authorization : Bearer <JWT_TOKEN> => 이렇게 생겼기 때문
			//접두어를 제거하고 실제 토큰 취함
		} else {
			//쿠키에서 토큰 얻기
			Cookie[] cookies = request.getCookies();
			if(cookies != null) {
				for(Cookie cookie : cookies) {
					if("JWT".equals(cookie.getName())) {
						token = cookie.getValue();
					}
				}
			}
		} 
		//토큰 검증 및 인증
		if(token != null && jwtUtil.validateToken(token)) {
			String sabun = jwtUtil.extractUsername(token);
			UserDetails userDetails = userDetailsService.loadUserByUsername(sabun);
			
			Authentication authentication 
				= new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
			
			//요청 처리 동안, 인증된 사용자 정보가 SecurityContext에 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		//FilterChain은 여러개의 필터가 순차적으로 실행되는 구조다. 각 필터는 HTTP 요청 처리 후 다음 필터로 요청을 전달함
		filterChain.doFilter(request, response); //JWT를 사용하며 stateless 정책을 쓰기 때문에 토큰 기반 인증을 간단히 처리
		
	}
}
