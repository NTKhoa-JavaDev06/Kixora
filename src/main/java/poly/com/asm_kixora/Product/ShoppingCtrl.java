package poly.com.asm_kixora.Product;

import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import poly.com.asm_kixora.entity.Cart;
import poly.com.asm_kixora.entity.CartItem;

@Controller
public class ShoppingCtrl {

    @Autowired
    ShoppingCartService shoppingCartService;

        @GetMapping("/view")
        public String viewcart(Model model){
            model.addAttribute("items", shoppingCartService.getItems());
            model.addAttribute("total", shoppingCartService.getAmount());
            model.addAttribute("count", shoppingCartService.getCount());
            return "cart/index";
        }
    @PostMapping("/add")
    public String addToCart(@ModelAttribute CartItem item) {
        shoppingCartService.add(item);
        return "redirect:/cart/view";
    }
    // 3. Xóa một sản phẩm
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id) {
        shoppingCartService.remove(id);
        return "redirect:/cart/view";
    }

    // 4. Cập nhật số lượng
    @PostMapping("/update")
    public String update(@RequestParam("id") Integer id, @RequestParam("qty") Integer qty) {
        shoppingCartService.update(id, qty);
        return "redirect:/cart/view";
    }

    // 5. Xóa sạch giỏ
    @GetMapping("/clear")
    public String clear() {
        shoppingCartService.clear();
        return "redirect:/cart/view";
    }
}

