package poly.com.asm_kixora.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    // Chi tieet san pham
    @GetMapping("/product/detail")
    public String chitiet(@RequestParam("id") Integer id, Model model) {
        // tim theo id
        Optional<Products> product = productRepository.findById(id);
        // neu san pham co
        if(product.isPresent() ){
            // thi lay san pham
            Products p = product.get();
            model.addAttribute("p", p);
            // dua len form voi truong p

            // sau do tim san pham co id khop
            List<ProductVariants> variants = productVariantRepository.findByProductId(id);
            model.addAttribute("variants", variants);


            if (p.getCategory() != null) {
                Integer categoryId = p.getCategory().getId();
                List<Products> relatedProducts = productRepository.findTop6ByCategory_IdOrderByCreatedDateDesc(categoryId);

                model.addAttribute("related", relatedProducts);
            }
            return "product-detail";
        }
        return "redirect:/home";
    }
    @GetMapping("/sanpham")
    public String danhsachsanpham(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> size,
            @RequestParam(required = false) List<String> color,
            @RequestParam(required = false) List<String> gender,
            @RequestParam(required = false) List<String> material,
            @RequestParam(required = false) List<Integer> cid,
            @RequestParam(required = false) List<Integer> bid,
            @RequestParam(defaultValue = "0") int p,
            Model model) {

        List<String> sFilter = (size != null && !size.isEmpty()) ? size : null;
        List<String> cFilter = (color != null && !color.isEmpty()) ? color : null;
        List<String> gFilter = (gender != null && !gender.isEmpty()) ? gender : null;
        List<String> mFilter = (material != null && !material.isEmpty()) ? material : null;
        List<Integer> catFilter = (cid != null && !cid.isEmpty()) ? cid : null;
        List<Integer> brandFilter = (bid != null && !bid.isEmpty()) ? bid : null;

        Pageable pageable = PageRequest.of(p, 12);

        Page<Products> pageData = productRepository.findFilteredProducts(
                sFilter, cFilter, gFilter, mFilter, keyword,
                catFilter, brandFilter, null, null, pageable
        );

        model.addAttribute("items", pageData.getContent());
        model.addAttribute("page", pageData);
        model.addAttribute("keyword", keyword);


        model.addAttribute("allBrands", productRepository.findAllBrands());
        model.addAttribute("allCategories", catagoriesSevive.findAll());
        model.addAttribute("allMaterials", productRepository.findAllExistingMaterials());
        model.addAttribute("allGenders", productRepository.findAllExistingGenders());
        model.addAttribute("allSizes", productRepository.findAllExistingSizes());
        model.addAttribute("allColors", productRepository.findAllExistingColors());

        return "product/sanpham";
    }
}

