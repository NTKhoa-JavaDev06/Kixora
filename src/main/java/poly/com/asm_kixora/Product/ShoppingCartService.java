package poly.com.asm_kixora.Product;

import poly.com.asm_kixora.entity.CartItem;

import java.util.Collection;

public interface ShoppingCartService {
    void add(CartItem item);
    void remove(Integer id);
    CartItem update(Integer id, int qty); // Cập nhật số lượng
    void clear();                       // Xóa sạch giỏ hàng
    Collection<CartItem> getItems();    // Lấy tất cả mặt hàng
    int getCount();                     // Tổng số lượng (ví dụ: 3 cái áo)
    double getAmount();
}
