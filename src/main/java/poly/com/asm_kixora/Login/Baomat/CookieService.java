package poly.com.asm_kixora.Login.Baomat;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;

    /**
     * Tạo và gửi cookie về trình duyệt
     * @param name tên cookie
     * @param value giá trị cookie
     * @param days số ngày tồn tại
     */
    public void create(String name, String value, int days) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(days * 24 * 60 * 60); // Chuyển từ ngày sang giây
        cookie.setPath("/"); // Áp dụng cho toàn bộ domain
        response.addCookie(cookie);
    }

    /**
     * Đọc giá trị cookie từ request
     * @param name tên cookie cần đọc
     * @return giá trị cookie hoặc null nếu không tồn tại
     */
    public String get(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Xóa cookie khỏi trình duyệt
     * @param name tên cookie cần xóa
     */
    public void remove(String name) {
        // Để xóa cookie, ta tạo lại cookie đó với MaxAge = 0
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}