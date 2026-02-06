package poly.com.asm_kixora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "CartItems")
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng ID
    @Column(name = "Id")
    private Integer id;

    // Thay vì lưu int, ta map quan hệ với bảng Cart
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // Thay vì lưu int, ta map quan hệ với ProductVariants để lấy thông tin Size/Màu
    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariants productVariant;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "added_date")
    private LocalDateTime addedDate;

    public Double totalAmount(){
        if(productVariant != null && productVariant.getProduct() != null){
            double price = productVariant.getProduct().getPrice().doubleValue();
            return price * quantity;
        }
        return 0.0;
    }
}