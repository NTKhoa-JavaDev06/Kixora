package poly.com.asm_kixora.Voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.com.asm_kixora.entity.UserVouchers;
import poly.com.asm_kixora.entity.Vouchers;

import java.util.List;

public interface UserVouchersRepository extends JpaRepository<UserVouchers, String>   {
    List<UserVouchers> findByUserIdAndIsUsedFalse(Integer userId);

    @Query
            ("Select uv from UserVouchers uv Join fetch uv.voucher Where uv.userId = :userId And uv.voucherCode = :code")
    UserVouchers takevoucherstouse(@Param("userId") Integer userId, @Param("code") String code);

    @Query("Select uv.voucherCode from UserVouchers uv WHERE  uv.userId = :userId")
    List<String> findSavedVoucherCoder(@Param("userId") Integer userId);

    @Query("SELECT uv FROM UserVouchers uv JOIN FETCH uv.voucher WHERE uv.userId = :userId AND (uv.isUsed = false OR uv.isUsed IS NULL)")
    List<UserVouchers> findAvailableVouchersByUserId(@Param("userId") Integer userId);
    boolean existsByUserIdAndVoucherCode(Integer userId, String voucherCode);
}
