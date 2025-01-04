package pack.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import pack.util.JwtUtil;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	
	public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@PostMapping("/login")
	public String performLogin(@RequestParam(name="sabun")String sabun,
			@RequestParam(name="irum")String irum, Model model, HttpServletResponse response) {
		try {
			//두 개의 매개변수를 기반으로 인증 토큰을 생성
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(sabun, irum);
			
			//인증 매니저로 인증 시도
			Authentication authentication = authenticationManager.authenticate(token);
			//CustomUserDetailService의 loadUserByUsername()을 호출하여 사용자 정보를 얻음
			
			//jwt 생성
			String jwt = jwtUtil.generateToken(sabun);
			
			//생성된 jwt를 쿠키에 저장
			Cookie jwtCookie = new Cookie("JWT", jwt);
			jwtCookie.setHttpOnly(true);
			jwtCookie.setSecure(false); //HTTPS에서만 사용할 경우 true
			jwtCookie.setPath("/");
			jwtCookie.setMaxAge(60*60); //1시간 유효 (지정안해주면 1년이 기본)
			response.addCookie(jwtCookie);
			
			model.addAttribute("username",authentication.getName());
			
			return "redirect:/auth/success";
			//return "success";
			
		} catch (AuthenticationException e) {
			model.addAttribute("error", "로그인 실패");
			return "login";
		}
	}
	
	@GetMapping("/success")
	public String success(Model model, Authentication authentication) {
		if(authentication != null && authentication.isAuthenticated()) {
			model.addAttribute("username",authentication.getName());
			return "success";
		}
		return "redirect:/auth/login"; //인증이 안됐을때에는 login 페이지로 돌아감
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletResponse response) {
		//jwt 쿠키 제거
		Cookie jwtCookie = new Cookie("JWT", null);
		jwtCookie.setHttpOnly(true);
		jwtCookie.setSecure(false);
		jwtCookie.setPath("/");
		jwtCookie.setMaxAge(0); //유효시간 즉시 만료
		response.addCookie(jwtCookie);
		
		//SecurityContextHolder 초기화
		SecurityContextHolder.clearContext();
		
		return "redirect:/auth/login";
	}
	
	@GetMapping("/gugu")
	public String gugu(Model model, Authentication authentication) {
		if(authentication == null || !authentication.isAuthenticated()) {
			return "redirect:/auth/login";
		}
		model.addAttribute("username",authentication.getName());
		return "gugu";
	}
	
	@PostMapping("/gugu")
	public String guguResult(@RequestParam(name="num")int num, Model model, Authentication authentication) {
		if(authentication == null || !authentication.isAuthenticated()) {
			return "redirect:/auth/login";
		}
		model.addAttribute("username",authentication.getName());
		model.addAttribute("num",num);
		return "guguresult";
	}
}
