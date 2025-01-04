package pack;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class TestController {
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	//Authentication : 현재 로그인한 사용자에 대한 정보를 가짐
	@GetMapping("/default")
	public void defaultAfterLogin(Authentication authentication,
			HttpServletResponse response) throws IOException {
		for(GrantedAuthority authority : authentication.getAuthorities()) { //사용자의 역할을 가져오게됨
			String role = authority.getAuthority(); //특정 권한(ROLE)을 문자열로 반환
			System.out.println("role : " + role);
			
			if(role.equals("ROLE_ADMIN")) {
				response.sendRedirect("/admin");
			} else if (role.equals("ROLE_USER")) {
				response.sendRedirect("/user");
			} else if (role.equals("ROLE_JAMES")) {
				response.sendRedirect("/james");
			} else { response.sendRedirect("/common"); }
		}
	}
	
	@GetMapping("/admin")
	public String adminPage(Model model) {
		model.addAttribute("msg", "관리자권한");
		return "common";
	}
	
	@GetMapping("/user")
	public String userPage(Model model) {
		model.addAttribute("msg", "사용자권한");
		return "common";
	}
	
	@GetMapping("/james")
	public String jamesPage(Model model) {
		model.addAttribute("msg", "james권한");
		return "common";
	}
	
	@GetMapping("/common")
	public String commonPage(Model model) {
		model.addAttribute("msg", "모두에게 허용된 페이지");
		return "common";
	}
}
