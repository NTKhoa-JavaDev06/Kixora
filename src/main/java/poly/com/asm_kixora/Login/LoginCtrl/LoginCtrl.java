package poly.com.asm_kixora.Login.LoginCtrl;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String loginform(Model model) {
        String user = cookieService.get("user_Account");
        String pass = cookieService.get("user_Password");
        model.addAttribute("username", user != null ? user : "");
        model.addAttribute("password", pass != null ? pass : "");
        return "Login/login";
    }

    @RequestMapping("/login/check")
    public String logincheck(@RequestParam("username") String email,
                             @RequestParam("password") String password,
                             @RequestParam(value = "remember", required = false) String remember,
                             Model model) {

        Optional<Accounts> accOpt = res.findByEmail(email);

        if (accOpt.isPresent()) {
            Accounts acc = accOpt.get();
            // So sánh mật khẩu (Bro nên dùng BCrypt nếu làm thực tế nhé)
            if (acc.getPassword().equals(password)) {
                session.setAttribute("user", acc);

                // Xử lý Ghi nhớ mật khẩu
                if (remember != null) {
                    cookieService.create("user_Account", email, 10); // 10 ngày
                    cookieService.create("user_Password", password, 10);
                } else {
                    cookieService.remove("user_Account");
                    cookieService.remove("user_Password");
                }

                // Điều hướng dựa trên Role
                if ("Admin".equalsIgnoreCase(acc.getRole())) {
                    return "redirect:/admin/dashboard";
                } else {
                    return "redirect:/home"; // Đẩy về trang chủ cho User
                }
            }
        }

        // Nếu sai tài khoản/mật khẩu
        model.addAttribute("msg", "Tài khoản hoặc mật khẩu không chính xác!");
        return "Login/login"; // Trả về trang login để hiện thông báo lỗi
    }

    @GetMapping("/login/google/success")
    public String googleLoginSuccess(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return "redirect:/Login/login";
        }

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");

        Optional<Accounts> accOpt = res.findByEmail(email);
        Accounts account;

        if (accOpt.isPresent()) {
            account = accOpt.get();
        } else {
            // Tự tạo account mới nếu chưa có trong DB
            account = new Accounts();
            account.setEmail(email);
            account.setFullname(name);
            account.setPhoto(picture);
            account.setPassword("");
            account.setRole("User");
            account.setActivated(true);
            res.save(account);
        }

        session.setAttribute("user", account);

        // Điều hướng
        if ("Admin".equalsIgnoreCase(account.getRole())) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/home";
    }

    @RequestMapping("/login/logout")
    public String logout() {
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:/Login/login";
    }
}