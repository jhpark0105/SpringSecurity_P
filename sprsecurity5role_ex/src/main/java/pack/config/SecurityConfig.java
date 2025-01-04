package pack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	// 스프링 시큐리티는 시큐리티 관련해 다양한 기능을 필터 체인(클라이언트요청 -> 필터 -> 필터 -> 서블릿)
	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		//white list 목록
		String[] whiteList = {
			"/","/notice","/user/loginform", "/user/login_fail", "/user/expired", "/shop"	
		};
		
		httpSecurity.csrf(csrf -> csrf.disable())
					.authorizeHttpRequests(config -> 
						config
							.requestMatchers(whiteList).permitAll()
							.requestMatchers("/admin/**").hasRole("ADMIN")
							.requestMatchers("/staff/**").hasAnyRole("ADMIN","STAFF")
							.anyRequest().authenticated()
					)
					.formLogin(config -> 
				    	config
				        	.loginPage("/user/required_loginform")
				        	.loginProcessingUrl("/user/login")
				        	.usernameParameter("userName")
				            .passwordParameter("password")
				        	.successHandler(new AuthSuccessHandler())
				        	.failureForwardUrl("/user/login_fail")
				        	.permitAll()
					)
					.logout(config -> 
						config
							.logoutUrl("/user/logout") //로그아웃은 sercurity가 알아서 해주기때문에 경로만 찍어주고 html을 안만들어도된다
							.logoutSuccessUrl("/")
							.permitAll()
					)
					.exceptionHandling(config -> //인증 처리중 예외가 발생했을 때, 관련 설정
						//얘는 ExceptionHandlingConfigurer를 반환,
						//ExceptionHandlingConfigurer 객체의 accessDeniedPage()를 이용해 403인 경우 forward할 경로 지정
						config.accessDeniedPage("/user/denied")
					)
					.sessionManagement(config ->
						config
							.maximumSessions(1) //최대 허용 세션 수
							.expiredUrl("/user/expired")
					);
		
		return httpSecurity.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity httpSecurity
			,UserDetailsService userDetailsService,
			BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder 
			= httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
		//AuthenticationManagerBuilder는 UserDetailsService를 필요로 한다.
		authenticationManagerBuilder.userDetailsService(userDetailsService)
									.passwordEncoder(bCryptPasswordEncoder);
		return authenticationManagerBuilder.build();
	}
}
