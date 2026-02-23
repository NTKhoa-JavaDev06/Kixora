package poly.com.asm_kixora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Quan trọng: Tự động tăng ID
    @Column(name = "Id")
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "UserId")
    private Accounts account;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

    // (Optional) Map ngược lại để từ Cart lấy được danh sách CartItems bên trong
    @OneToMany(mappedBy = "cart")
    private List<CartItems> cartItems;

    public void setUserId(Integer userId) {
        if (userId != null) {
            Accounts acc = new Accounts();
            acc.setId(userId);
            this.account = acc;
        }
    }
}