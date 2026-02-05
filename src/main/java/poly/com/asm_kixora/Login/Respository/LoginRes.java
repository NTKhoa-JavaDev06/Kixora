package poly.com.asm_kixora.Login.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.com.asm_kixora.Login.Respository.LoginRes;
import poly.com.asm_kixora.entity.Accounts;

import java.util.Optional;

public interface LoginRes extends JpaRepository<Accounts, String> {
    Optional<Accounts> findByEmail(String email);
}
