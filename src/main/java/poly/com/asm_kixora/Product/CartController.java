package poly.com.asm_kixora.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import poly.com.asm_kixora.entity.CartItem;
import poly.com.asm_kixora.entity.ProductVariants;
import poly.com.asm_kixora.entity.Products;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ShoppingCartService cartService;
    @Autowired ProductRepository productRepo;
    @Autowired ProductVariantRepository variantRepo;

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Integer productId,
                            @RequestParam("variantId") Integer variantId,
                            @RequestParam("quantity") Integer quantity) {

        // 1. Tìm Product và Variant trong Database
        Optional<Products> pOpt = productRepo.findById(productId);
        Optional<ProductVariants> vOpt = variantRepo.findById(variantId);

        if (pOpt.isPresent() && vOpt.isPresent()) {
            Products p = pOpt.get();
            ProductVariants v = vOpt.get();

            CartItem item = new CartItem();
            item.setVariantId(v.getId());
            item.setProductId(p.getId());
            item.setName(p.getName());
            item.setImage(p.getImage());
            item.setPrice(p.getPrice().doubleValue());
            item.setSize(v.getSize());
            item.setColor(v.getColor());
            item.setQuantity(quantity);

            // 3. Gọi Service để thêm vào giỏ hàng Session
            cartService.add(item);
        }

        // 4. Chuyển hướng sang trang xem giỏ hàng
        return "redirect:/cart/view";
    }

    @GetMapping("/view")
    public String viewCart(Model model) {
        model.addAttribute("items", cartService.getItems());
        model.addAttribute("total", cartService.getAmount());
        model.addAttribute("cartCount", cartService.getCount());
        return "cart/index"; // Trả về trang giỏ hàng màu Xanh - Đen của bạn
    }
//    @RequestParam("/cart/remove/${id}")
//    public String delete(Model model){
//
//    }
}