package poly.com.asm_kixora.Voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import poly.com.asm_kixora.entity.Vouchers;
import poly.com.asm_kixora.entity.UserVouchers;
import java.util.List;

@Repository
public interface VoucherReprository extends JpaRepository<Vouchers, Integer> {

    static void save(UserVouchers uv) {
    }

    @Query("Select v from Vouchers v Where v.isActive = true And v.expiryDate > CURRENT_TIMESTAMP ORDER BY v.discountAmount DESC")
    List<Vouchers> findAvailableVouchers();



}
