package poly.com.asm_kixora.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.com.asm_kixora.entity.ProductVariants;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariants, Integer > {
    List<ProductVariants> findByProductId(Integer productId);
}
