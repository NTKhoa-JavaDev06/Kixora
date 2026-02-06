package poly.com.asm_kixora.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.com.asm_kixora.entity.Banners;

import java.util.List;

public interface BannerReponsitory extends JpaRepository<Banners, Integer> {
    List<Banners> findByActiveTrue();

}
