package poly.com.asm_kixora.Login.LoginCtrl;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import poly.com.asm_kixora.Login.Baomat.CookieService;
import poly.com.asm_kixora.Login.Respository.LoginRes;
import poly.com.asm_kixora.entity.Accounts;

import java.util.Optional;

@Controller
public class LoginCtrl {
    @Autowired
    LoginRes res;

    @Autowired
    HttpSession session;


    @Autowired
    private CookieService cookieService;

    @GetMapping("/Login/login")
    public String loginform(Model model){
        String user = cookieService.get("user_Account");
        String pass = cookieService.get("user_Password");
        model.addAttribute("username", user != null ? user: "" );
        model.addAttribute("password", pass != null ? pass: "" );
        return "Login/login";
    }

    @RequestMapping("/trangchu/index")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/login/check")
    public String logincheck(@RequestParam("username") String email,
                             @RequestParam("password") String password,
                             @RequestParam(value = "remember", required = false) String remember,
                             Model model) {

        Optional<Accounts> accOpt = res.findByEmail(email);

        if (accOpt.isPresent()) {
            Accounts acc = accOpt.get();
            if (acc.getPassword().equals(password)) {
                session.setAttribute("user", acc);

                if (remember != null) {
                    cookieService.create("user_Account", email, 10);
                    cookieService.create("user_Password", password, 10);
                } else {
                   cookieService.create("user_Account", "", 0);
                    cookieService.create("user_Password", "", 0);
                }

                if ("Admin".equalsIgnoreCase(acc.getRole())) {
                    model.addAttribute("msg", "Chào Admin!");
                      return "/admin/adminform";
                } else {
                    model.addAttribute("msg", "Đăng nhập thành công!");
                    return "redirect:/trangchu/index";
                }
            }
        }


        model.addAttribute("msg", "Tài khoản hoặc mật khẩu không chính xác!");
        return "redirect:/home";
    }

    @RequestMapping("/login/logout")
    public String logout() {
        session.removeAttribute("user");
        return "redirect:/login/form";
    }
}
