package pack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import pack.JwtAuthenticationFilter;
import pack.util.JwtUtil;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {
	private final JwtUtil jwtUtil;
	
	public SecurityConfig(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	// JWT 사용시 주요 사항
	// 1) 세션관리 비활성화 : 세션 대신 STATELESS 보안을 사용
	// 2) JWT 필터 추가 : 매 요청마다 JWT 토큰을 확인하고 인증 정보를 설정하기 위한 커스텀 필터를 만든다.
	// 3) 폼 기반 인증 제거 : JWT 인증에서는 로그인 페이지 보다는 RESTApi를 로그인 요청으로 처리 하므로 formLogin 설정을 제거
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, 
			JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception{
		http
			.csrf(csrf -> csrf.disable()) //JWT는 csrf 비활성화. 이유는 JWT 헤더를 인증하기 때문에 csrf 공격을 받지않음..
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/login","/auth/logout","/statis/**").permitAll() //권한 모두 허용
				.requestMatchers("/auth/gugu","/auth/guguresult","/auth/success").authenticated()) //인증된 사용자만 권한 허용
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션을 생성하지 않겠다는 설정
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		//AuthenticationManager는 인증 로직의 중심 역할을 하며, 요청을 처리한다.
				//AuthenticationConfiguration은 Spring Security의 AuthenticationManager를 설정 및 관리하는 도우미 역할을 한다.
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
