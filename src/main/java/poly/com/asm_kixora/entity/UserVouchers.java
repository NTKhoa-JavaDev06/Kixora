package poly.com.asm_kixora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UserVouchers")
@IdClass(UserVouchers.UserVoucherId.class)
public class UserVouchers {

    @Id
    @Column(name = "UserId")
    private Integer userId;

    @Id
    @Column(name = "VoucherCode")
    private String voucherCode;

    // THÊM ĐOẠN NÀY ĐỂ FIX LỖI: Liên kết trở lại bảng Vouchers
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VoucherCode", referencedColumnName = "Code", insertable = false, updatable = false)
    private Vouchers voucher;

    @Column(name = "AssignedAt")
    private LocalDateTime assignedAt;

    @Column(name = "IsUsed")
    private Boolean isUsed;

    @Column(name = "UsedAt")
    private LocalDateTime usedAt;

    // ... (Giữ nguyên class UserVoucherId bên dưới) ...
    public static class UserVoucherId implements Serializable {
        private Integer userId;
        private String voucherCode;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserVoucherId that = (UserVoucherId) o;
            return Objects.equals(userId, that.userId) && Objects.equals(voucherCode, that.voucherCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, voucherCode);
        }
    }
}