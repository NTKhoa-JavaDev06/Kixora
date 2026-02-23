package poly.com.asm_kixora.Order;


import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.com.asm_kixora.entity.OrderDetails;
import poly.com.asm_kixora.entity.Orders;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    @Query("SELECT SUM(o.totalAmount) FROM Orders o")
    Double getTotalRevenue();


    List<Orders> findTop5ByOrderByOrderDateDesc();
}
