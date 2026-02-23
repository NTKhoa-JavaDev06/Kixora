package poly.com.asm_kixora.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Integer variantId; // ID của biến thể (Size/Màu)
    private Integer productId;
    private String name;
    private String image;
    private Double price;
    private String size;
    private String color;
    private int quantity;
    private int stockQuantity;
    public Double getAmount() {
        return price * quantity;
    }
}