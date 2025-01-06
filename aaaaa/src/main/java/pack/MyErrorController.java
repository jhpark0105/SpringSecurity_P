package pack;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {
	@GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        // 모든 에러를 /login으로 리다이렉트
        return "redirect:/login";
    }
}
