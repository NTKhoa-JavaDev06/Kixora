package poly.com.asm_kixora.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.com.asm_kixora.entity.Products;

import java.util.List;

public interface ProductRepository extends JpaRepository<Products, Integer>
 {
  List<Products> findByCategory_Id(Integer cid);
}
