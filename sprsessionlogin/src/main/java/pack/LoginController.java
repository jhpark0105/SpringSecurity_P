package pack;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(name="userid") String userid,
                        @RequestParam(name="password") String password,
                        HttpSession session, Model model) {

        String validid = "ok";
        String validpwd = "111";

        if (userid.equals(validid) && password.equals(validpwd)) {
            session.setAttribute("userid", validid); // 세션에 userid 저장
            return "redirect:/success";
        } else {
            model.addAttribute("error", "입력 정보 오류");
            return "login";
        }
    }

    @GetMapping("/success")
    public String success(HttpSession session, Model model) {
        String user = (String) session.getAttribute("userid"); // "userid"로 가져오기

        if (user != null) {
            model.addAttribute("myuser", user);
            return "success";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userid"); // "userid" 속성 제거
        return "redirect:/login";
    }

    @GetMapping("/gugu")
    public String gugu(HttpSession session) {
        if (session.getAttribute("userid") != null) { // "userid"로 확인
            return "gugu";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/gugu")
    public String gugu(@RequestParam(name="dan") int dan,
                       HttpSession session, Model model) {

        if (session.getAttribute("userid") != null) { // "userid"로 확인
            model.addAttribute("dan", dan);
            return "guguresult";
        } else {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }
    }
}
