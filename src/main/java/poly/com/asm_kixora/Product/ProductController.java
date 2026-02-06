package poly.com.asm_kixora.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import poly.com.asm_kixora.entity.ProductVariants;
import poly.com.asm_kixora.entity.Products;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CatagoriesSevive catagoriesSevive;
    @Autowired
    ProductVariantRepository productVariantRepository;

    @GetMapping("/product/detail")
    public String chitiet(@RequestParam("id") Integer id, Model model) {
        Optional<Products> product = productRepository.findById(id);

        if(product.isPresent() ){
            Products p = product.get();
            model.addAttribute("p", p);

            List<ProductVariants> variants = productVariantRepository.findByProductId(id);
            model.addAttribute("variants", variants);

            if (p.getCategory() != null) {
                Integer categoryId = p.getCategory().getId();
                List<Products> relatedProducts = productRepository.findByCategory_Id(categoryId);
                model.addAttribute("related", relatedProducts);
            }
            return "product-detail";
        }
        return "redirect:/home";
    }
}
