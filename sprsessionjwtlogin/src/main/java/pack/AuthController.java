package pack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
	
	@Autowired
	private JwtService jwtService;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam("userid")String id, @RequestParam("password")String password,
			HttpServletResponse response) {
		String validId = "ok";
		String validPwd = "111";
		
		if(id.equals(validId) && password.equals(validPwd)) {
			String token = jwtService.createToken(id);
			Cookie cookie = new Cookie("jwt",token);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			response.addCookie(cookie);
			
			return "redirect:/success";
		} else {
			return "redirect:/login?error";
		}
	}
	
	@GetMapping("/success")
	public String success(HttpServletRequest request, Model model) {
		String userId = getuserIdFromToken(request);
		
		if(userId == null) {
			return "redirect:/login";
		}
		model.addAttribute("myuser",userId);
		return "success";
	}
	
	private String getuserIdFromToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals("jwt")) {
					String token = cookie.getValue();
					return jwtService.getUseridFromToken(token);
				}
			}
		}
		return null;
	}
	
	@GetMapping("/logout")
    public String logout(HttpServletResponse response) {
		//쿠키 무효화
		Cookie cookie = new Cookie("jwt", null);
		cookie.setMaxAge(0); //쿠키의 수명을 0 
		cookie.setPath("/");
		response.addCookie(cookie);
		
        return "redirect:/login";
    }

    @GetMapping("/gugu")
    public String gugu(HttpServletRequest request) {
    	String userId = getuserIdFromToken(request);
    	
        if (userId == null) {
            return "redirect:/login";
        } else {
            return "gugu";
        }
    }

    @PostMapping("/gugu")
    public String gugu(@RequestParam(name="dan") int dan,
    		HttpServletRequest request, Model model) {

    	String userId = getuserIdFromToken(request);
    	
    	if (userId == null) {
            return "redirect:/login";
        } 
    	
    	model.addAttribute("dan",dan);
        return "guguresult";
    }
	
}
