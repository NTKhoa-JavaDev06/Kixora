package poly.com.asm_kixora.Trangchu.HomeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.com.asm_kixora.Product.BannerReponsitory;
import poly.com.asm_kixora.Product.CatagoriesSevive; // Interface Repo
import poly.com.asm_kixora.Product.ProductRepository; // Interface Repo
import poly.com.asm_kixora.entity.Banners;
import poly.com.asm_kixora.entity.Categories; // Entity đúng
import poly.com.asm_kixora.entity.Products; // Entity đúng

import java.util.List;
import java.util.Optional;

@Controller
public class HomeCtrl {
    @Autowired
     ProductRepository productRepository;

    @Autowired
    CatagoriesSevive catagoriesSevive;
    @Autowired
    BannerReponsitory bannerRepository;

    @GetMapping("/home")
    public String listProducts(Model model,
                               @RequestParam("cid")Optional<Integer> cid) {
        List<Products> list;
        if (cid.isPresent()) {
            System.out.println(">> Đang lọc theo Category ID: " + cid.get());
            list = productRepository.findByCategory_Id(cid.get());
        } else {
            System.out.println(">> Đang lấy TẤT CẢ sản phẩm");
            list = productRepository.findAll();
        }
        System.out.println(">> Số lượng sản phẩm lấy được: " + list.size());
        List<Categories> categories = catagoriesSevive.findAll();
        model.addAttribute("items", list);
        model.addAttribute("categories", categories);
        List<Banners> banners = bannerRepository.findByActiveTrue();
        model.addAttribute("banners", banners);
        return "Home";

    }
}
