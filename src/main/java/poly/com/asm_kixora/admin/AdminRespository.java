package poly.com.asm_kixora.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.com.asm_kixora.entity.Accounts;

public interface AdminRespository extends JpaRepository<Accounts, Integer> {
}
