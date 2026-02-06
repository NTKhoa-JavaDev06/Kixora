package poly.com.asm_kixora.Nav;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import poly.com.asm_kixora.entity.Categories;
import poly.com.asm_kixora.entity.Products;

import java.util.ArrayList;
import java.util.List;
@ControllerAdvice
public class NavCtrl {
    @Autowired
    HttpSession session;

    @Autowired
    NavRes navres;

    @Autowired
    poly.com.asm_kixora.Catagory.Rep.CatagoryRes catagoryRes;
//
//    @RequestMapping("/trangchu/index")
//    public String index(Model model) {
//        List<Products> list = navres.findAll();
//        model.addAttribute("items", list);
//        return "Home";
//    }
    @ModelAttribute("allCategories")
    public List<Categories> GetAllCategories(){
        return catagoryRes.findAll();
    }

    @RequestMapping("/cart/add")
    public String addToCart(@RequestParam("id") Integer id) {
        Integer currentCount = (Integer) session.getAttribute("cartSize");
        if (currentCount == null) {
            currentCount = 0;
        }
        session.setAttribute("cartSize", currentCount + 1);
        return "redirect:/trangchu/index";
    }

}