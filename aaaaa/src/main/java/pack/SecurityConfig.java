package pack;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/", "/css/**", "/js/**").permitAll() // 로그인 페이지 허용
	            .anyRequest().authenticated())
	            //.requestMatchers("/api/movies/**").authenticated()) // 인증 필요
	        .formLogin(form -> form
	            .loginPage("/login").permitAll() // 커스텀 로그인 페이지
	            .defaultSuccessUrl("/api/movies", true)) // 로그인 성공 후 이동
	        .logout(logout -> logout.permitAll()) // 로그아웃 허용
	        .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/login");
                })); // 인증되지 않은 요청에 대해 /login으로 리다이렉트
	    return http.build();
	}
}
