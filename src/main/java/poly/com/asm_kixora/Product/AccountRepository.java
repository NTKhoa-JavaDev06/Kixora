package poly.com.asm_kixora.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.com.asm_kixora.entity.Accounts;

public interface AccountRepository extends JpaRepository<Accounts, String> {
}
