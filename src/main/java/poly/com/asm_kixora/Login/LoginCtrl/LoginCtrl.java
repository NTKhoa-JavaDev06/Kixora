package poly.com.asm_kixora.Login.LoginCtrl;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
// Nhớ thêm 2 cái import OAuth2 này nha bro
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
                      return "redirect:/admin/dashboard";
                } else {
                    model.addAttribute("msg", "Đăng nhập thành công!");
                    return "Home";
                }
            }
        }


        model.addAttribute("msg", "Tài khoản hoặc mật khẩu không chính xác!");
        return "redirect:/home";
    }
    @GetMapping("/login/google/success")
    public String googleLoginSuccess(@AuthenticationPrincipal OAuth2User oauth2User, HttpSession session) {

        // 1. Lấy thông tin từ tài khoản Google của khách
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture"); // Avatar Google

        System.out.println("🚀 [OAUTH2] Khách đang đăng nhập Google: " + email);

        // 2. Tìm trong Database xem khách này từng đăng nhập chưa
        Optional<Accounts> accOpt = res.findByEmail(email);
        Accounts account;

        if (accOpt.isPresent()) {
            // Nếu có rồi thì lấy ra xài
            account = accOpt.get();
        } else {
            // Nếu là khách mới tinh -> Tự động tạo tài khoản cho họ
            account = new Accounts();
            account.setEmail(email);
            account.setFullname(name);
            account.setPhoto(picture);
            account.setPassword(""); // Login Google thì không cần password
            account.setRole("User");
            account.setActivated(true);

            res.save(account); // Lưu thẳng xuống SQL Server
            System.out.println("✅ [OAUTH2] Đã tự động tạo tài khoản mới cho: " + email);
        }

        // 3. Đưa thông tin vào Session (giống hệt cách đăng nhập truyền thống)
        session.setAttribute("user", account);


        if ("Admin".equalsIgnoreCase(account.getRole())) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/home";
    }
    @RequestMapping("/login/logout")
    public String logout() {

        session.invalidate();
       return "redirect:/Login/login";
    }
}
