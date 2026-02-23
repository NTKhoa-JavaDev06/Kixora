package poly.com.asm_kixora.Voucher;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poly.com.asm_kixora.entity.Accounts;
import poly.com.asm_kixora.entity.UserVouchers;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class VoucherCtrl {

    @Autowired
    VoucherReprository voucherReprository;

    @Autowired
    UserVouchersRepository userVouchersRepository;
    @Autowired
    private HttpSession httpSession;

    @GetMapping("/san-vouchers")
    public String sanvoucher(Model model, HttpSession httpSession){
        model.addAttribute("vouchers", voucherReprository.findAvailableVouchers());

        List<String> savedCodes = Optional.ofNullable((Accounts) httpSession.getAttribute("user"))
                .map(user -> userVouchersRepository.findSavedVoucherCoder(user.getId()))
                .orElse(new ArrayList<>());
        model.addAttribute("savedCodes", savedCodes);

        return "Vouchers/san-vouchers";
    }

    @GetMapping("/luu-voucher")
    public String luuvoucher(@RequestParam("code") String code, HttpSession httpSession, RedirectAttributes redirectAttributes){

        Accounts user = (Accounts) httpSession.getAttribute("user");

        if (user == null) {
            return "redirect:/Login/login";
        }

        boolean daLuu = userVouchersRepository.existsByUserIdAndVoucherCode(user.getId(), code);

        if (daLuu) {
            redirectAttributes.addFlashAttribute("error", "Bro đã lưu mã [" + code + "] này rồi!");
        } else {

            UserVouchers uv = new UserVouchers();
            uv.setUserId(user.getId());
            uv.setVoucherCode(code);
            uv.setAssignedAt(LocalDateTime.now());
            uv.setIsUsed(false);

            userVouchersRepository.save(uv);

            redirectAttributes.addFlashAttribute("success", "Lưu mã [" + code + "] thành công!");
        }

        return "redirect:/san-vouchers";
    }
}