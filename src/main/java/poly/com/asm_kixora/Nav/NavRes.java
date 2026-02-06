package poly.com.asm_kixora.Nav;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.com.asm_kixora.entity.Products;

import java.util.List;
import java.util.Optional;

public interface NavRes extends JpaRepository<Products, Integer> {
}
