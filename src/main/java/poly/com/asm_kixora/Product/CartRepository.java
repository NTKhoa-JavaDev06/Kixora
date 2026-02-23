package poly.com.asm_kixora.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.com.asm_kixora.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByAccount_Id(Integer userId);
}
