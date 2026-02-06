package poly.com.asm_kixora.Product;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import poly.com.asm_kixora.entity.CartItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope
public class ShoppingCartIplm implements ShoppingCartService {
    private Map<Integer, CartItem> map = new HashMap<>();
    @Override
    public void add(CartItem item) {
        // Kiểm tra xem variant này đã có trong giỏ chưa
        CartItem existItem = map.get(item.getVariantId());
        if (existItem != null) {
            // Nếu có rồi thì cộng thêm số lượng khách mới chọn
            existItem.setQuantity(existItem.getQuantity() + item.getQuantity());
        } else {
            // Nếu chưa có thì bỏ vào giỏ
            map.put(item.getVariantId(), item);
        }
    }

    @Override
    public void remove(Integer id) {
        map.remove(id);
    }

    @Override
    public CartItem update(Integer id, int qty) {
        CartItem item = map.get(id);
        if (item != null) {
            item.setQuantity(qty);
        }
        return item;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Collection<CartItem> getItems() {
        return map.values();
    }

    @Override
    public int getCount() {
        // Tổng số món hàng (ví dụ: mua 2 áo đỏ + 1 áo xanh = 3 sản phẩm)
        return map.values().stream()
                .mapToInt(item -> item.getQuantity())
                .sum();
    }

    @Override
    public double getAmount() {
        // Tổng số tiền khách phải trả
        return map.values().stream()
                .mapToDouble(item -> item.getAmount())
                .sum();
    }
}
