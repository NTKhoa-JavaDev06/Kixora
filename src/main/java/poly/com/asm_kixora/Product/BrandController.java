package poly.com.asm_kixora.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BrandController {

    @Autowired
    CatagoriesSevive catagoriesSevive;
    @Autowired
    BrandRepository brandRepository;



}
