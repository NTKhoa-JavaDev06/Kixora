package poly.com.asm_kixora.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.com.asm_kixora.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

}
