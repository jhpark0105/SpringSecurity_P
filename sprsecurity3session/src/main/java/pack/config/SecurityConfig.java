package pack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception{
		//AuthenticationManager는 인증 로직의 중심 역할을 하며, 요청을 처리한다.
		//AuthenticationConfiguration은 Spring Security의 AuthenticationManager를 설정 및 관리하는 도우미 역할을 한다.
		return authConf.getAuthenticationManager();

	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/login","/auth/logout","/statis/**").permitAll()
				.anyRequest().authenticated())
		.formLogin(formLogin -> formLogin
				.loginPage("/auth/login")
				.loginProcessingUrl("/auth/login")
				.usernameParameter("sabun")
				.passwordParameter("irum")
				.defaultSuccessUrl("/auth/success",true)
				.permitAll())
		.logout(logout -> logout
				.logoutUrl("/auth/logout")
				.logoutSuccessUrl("/auth/login")
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID")
				.permitAll());
		return http.build();
	}
	
	   @Bean
	   public PasswordEncoder passwordEncoder() {
		   return new BCryptPasswordEncoder();
	   }
}
