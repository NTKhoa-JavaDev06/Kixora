package poly.com.asm_kixora.Product;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.com.asm_kixora.Voucher.UserVouchersRepository;
import poly.com.asm_kixora.entity.*;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired UserVouchersRepository userVouchersRepository;
    @Autowired ShoppingCartService cartService;

    @RequestMapping("/add")
    public String addToCart(@RequestParam("variantId") Integer variantId,
                            @RequestParam("quantity") Integer quantity,
                            @RequestParam(value = "redirect", required = false) String redirect,
                            HttpSession session) {
        Accounts user = (Accounts) session.getAttribute("user");
        if (user == null) return "redirect:/Login/login";

        cartService.add(user.getId(), variantId, quantity);
        if("checkout".equals(redirect)){
            return "redirect:/cart/checkout";
        }
        return "redirect:/cart/view";
    }

    @GetMapping("/view")
    public String viewCart(Model model, HttpSession session) {
        Accounts user = (Accounts) session.getAttribute("user");
        if (user == null) return "redirect:/Login/login";

        model.addAttribute("items", cartService.getItems(user.getId()));
        model.addAttribute("total", cartService.getAmount(user.getId()));
        return "cart/index";
    }

    @GetMapping("/remove/{variantId}")
    public String remove(@PathVariable("variantId") Integer variantId, HttpSession session) {
        Accounts user = (Accounts) session.getAttribute("user");
        if (user != null) cartService.remove(user.getId(), variantId);
        return "redirect:/cart/view";
    }

    @PostMapping("/update")
    public String update(@RequestParam("variantId") Integer variantId,
                         @RequestParam("qty") Integer qty, HttpSession session) {
        Accounts user = (Accounts) session.getAttribute("user");
        if (user != null) cartService.update(user.getId(), variantId, qty);
        return "redirect:/cart/view";
    }

    @GetMapping("/clear")
    public String clear(HttpSession session) {
        Accounts user = (Accounts) session.getAttribute("user");
        if (user != null) cartService.clear(user.getId());
        return "redirect:/cart/view";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session, RedirectAttributes redirectAttributes){
        Accounts user =  (Accounts) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Bạn chưa đăng nhập");
            return "redirect:/Login/login";
        }

        List<CartItem> myItems = cartService.getItems(user.getId());
        if(myItems.isEmpty()){
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống trơn, mua đồ đi bro!");
            return "redirect:/cart/view";
        }

        model.addAttribute("items", myItems);
        model.addAttribute("total", cartService.getAmount(user.getId()));

        List<UserVouchers> userVouchers = userVouchersRepository.findAvailableVouchersByUserId(user.getId());
        model.addAttribute("myVouchers", userVouchers);

        return "cart/checkout";
    }
}