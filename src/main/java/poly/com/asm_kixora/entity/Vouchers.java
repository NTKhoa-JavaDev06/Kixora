package poly.com.asm_kixora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data; // Thêm dòng này
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data // Đặt ở đây để nó tự sinh Getter/Setter cho toàn bộ class
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Vouchers")
public class Vouchers {
    @Id
    @Column(name = "Code")
    private String code;

    @Column(name = "DiscountAmount")
    private BigDecimal discountAmount;

    @Column(name = "ExpiryDate")
    private LocalDateTime expiryDate;

    @Column(name = "IsActive")
    private Boolean isActive;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "voucher")
    private List<UserVouchers> userVouchers;

}