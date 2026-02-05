package poly.com.asm_kixora.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserVouchers")
public class UserVouchers {
    @Id
    @Column(name = "UserId")
    private Integer userId;

    @Id
    @Column(name = "VoucherCode")
    private String voucherCode;

    @Column(name = "AssignedAt")
    private LocalDateTime assignedAt;

    @Column(name = "IsUsed")
    private Boolean isUsed;

    @Column(name = "UsedAt")
    private LocalDateTime usedAt;

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getVoucherCode() {
        return this.voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public LocalDateTime getAssignedAt() {
        return this.assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Boolean getIsUsed() {
        return this.isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public LocalDateTime getUsedAt() {
        return this.usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }
}
