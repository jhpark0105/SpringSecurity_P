package pack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
	@GetMapping("/staff/user/list")
	public String userlist() {
		return "user/list";
	}
	
	@GetMapping("/admin/user/manage")
	public String usermanage() {
		return "user/manage";
	}
	
	@GetMapping("/user/required_loginform")
	public String required_loginform() {
		return "user/required_loginform";
	} 
	
	@GetMapping("/user/loginform")
	public String loginform() {
		return "user/loginform";
	}
	
	@PostMapping("/user/login_success")
	public String login_success() {
		return "user/login_success";
	}
	
	@GetMapping("/user/login_fail")
	public String login_fail() {
		return "user/login_fail";
	}
	
	@GetMapping("/user/denied") //권한이 부족할때에는 denied를 만남
	public String denied() {
		return "user/denied";
	}
	
	@GetMapping("/user/expired") //세션 허용 갯수 초과 시
	public String expired() {
		return "user/expired";
	}
}
