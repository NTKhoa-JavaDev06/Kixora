package poly.com.asm_kixora.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.com.asm_kixora.entity.CartItems;

import java.util.List;

public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {

    CartItems findByCart_IdAndProductVariant_Id(Integer cartId, Integer variantId);

    List<CartItems> findByCart_Id(Integer id);
}
