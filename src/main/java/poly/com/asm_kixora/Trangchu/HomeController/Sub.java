package poly.com.asm_kixora.Trangchu.HomeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/api")
public class Sub {
    @Autowired
    private JavaMailSender mailSender;
    private List<String> subscribedEmails = new ArrayList<>();

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestParam("email") String email) {

        if (subscribedEmails.contains(email)) {
            return ResponseEntity.badRequest().body("Email này đã đăng ký nhận mã rồi bro ơi!");
        }

        subscribedEmails.add(email);
        System.out.println("Khách mới: " + email);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("nguyenkhanh7510@gmail.com");
            message.setTo(email);
            message.setSubject("Đăng ký thành công - KIXORA Voucher");

            // Nội dung thư
            String text = "Chào bro!\n\n"
                    + "Cảm ơn bro đã đăng ký nhận thông báo từ KIXORA.\n"
                    + "Đây là email tự động. Mỗi khi hệ thống có đợt Sale hoặc Voucher mới siêu khủng, "
                    + "chúng mình sẽ ưu tiên gửi mã bí mật qua email này cho bro ngay lập tức!\n\n"
                    + "Giữ liên lạc nhé,\n"
                    + "KIXORA Team 👟";
            message.setText(text);


            mailSender.send(message);
            System.out.println("✅ Đã gửi email thành công tới: " + email);

        } catch (Exception e) {
            System.out.println("❌ Gửi mail thất bại: " + e.getMessage());
        }

       return ResponseEntity.ok("Thành công");
    }
}

