package poly.com.asm_kixora.Product;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import poly.com.asm_kixora.entity.Accounts;

@ControllerAdvice
public class GlobalInterceptor {

    @Autowired ShoppingCartService cartService;
    @Autowired BrandRepository brandRepository;
    @Autowired CatagoriesSevive catagoriesSevive;
    @Autowired HttpServletRequest request;

   @ModelAttribute("cartCount")
    public int globalCartCount(HttpSession session) {
        Accounts user = (Accounts) session.getAttribute("user");
        if (user != null) {
            return cartService.getCount(user.getId());
        }
        return 0;
    }

    @ModelAttribute
    public void addGlobalInterceptor(Model model, HttpServletRequest httpServletRequest){
        model.addAttribute("categories", catagoriesSevive.findAll());
        model.addAttribute("brands", brandRepository.findAll());

        String keyword = request.getParameter("keyword");
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", request.getParameter("min"));
        model.addAttribute("maxPrice", request.getParameter("max"));
    }
}