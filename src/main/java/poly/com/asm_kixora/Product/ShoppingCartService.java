package poly.com.asm_kixora.Product;

import poly.com.asm_kixora.entity.CartItem;
import java.util.List;

public interface ShoppingCartService {
    void add(Integer userId, Integer variantId, int quantity);
    void remove(Integer userId, Integer variantId);
    void update(Integer userId, Integer variantId, int qty);
    void clear(Integer userId);

    List<CartItem> getItems(Integer userId);
    int getCount(Integer userId);
    double getAmount(Integer userId);
}