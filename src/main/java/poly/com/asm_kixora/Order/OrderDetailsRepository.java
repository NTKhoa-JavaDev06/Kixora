package poly.com.asm_kixora.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.com.asm_kixora.entity.OrderDetails;
import java.util.List;
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {

    List<OrderDetails> findByOrderId(Integer orderId);
}
