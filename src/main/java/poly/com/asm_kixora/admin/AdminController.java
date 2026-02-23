package poly.com.asm_kixora.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import poly.com.asm_kixora.Order.OrderDetailsRepository;
import poly.com.asm_kixora.Order.OrdersRepository;
import poly.com.asm_kixora.Product.AccountRepository;
import poly.com.asm_kixora.Product.ProductRepository;
import poly.com.asm_kixora.entity.OrderDetails;
import poly.com.asm_kixora.entity.Orders;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired ProductRepository productRepo;
    @Autowired OrdersRepository orderRepo;
    @Autowired
    OrderDetailsRepository orderDetailsRepo;
    @Autowired AccountRepository accountRepository;

    // 1. DASHBOARD - Tổng quan số liệu
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Double rev = orderRepo.getTotalRevenue();
        List<Orders> orders = orderRepo.findTop5ByOrderByOrderDateDesc();

        model.addAttribute("revenue", rev != null ? rev : 0.0);
        model.addAttribute("totalOrders", orderRepo.count());
        model.addAttribute("totalProducts", productRepo.count());
        model.addAttribute("totalUsers", accountRepository.count());
        model.addAttribute("recentOrders", orders);

        return "admin/adminform";
    }
    @GetMapping("/orders/detail/{id}")
    public String orderDetail(@PathVariable("id") Integer id, Model model) {
        Orders order = orderRepo.findById(id).orElse(null);
        if (order == null) return "redirect:/admin/orders";

        List<OrderDetails> details = orderDetailsRepo.findByOrderId(id);
        model.addAttribute("order", order);
        model.addAttribute("details", details);

        return "admin/order-detail";
    }

    // 2. ORDERS - Danh sách tất cả đơn hàng
    @GetMapping("/orders")
    public String listOrders(Model model) {
        List<Orders> allOrders = orderRepo.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
        model.addAttribute("orders", allOrders);
        return "admin/order-list";
    }

    //  Cập nhật trạng thái
    @PostMapping("/orders/update-status")
    public String updateStatus(@RequestParam("orderId") Integer orderId,
                               @RequestParam("status") String status) {
        orderRepo.findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            if ("Done".equalsIgnoreCase(status)) {
                order.setPaymentStatus("Paid");
            }
            orderRepo.save(order);
        });
        return "redirect:/admin/orders";
    }

  @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "admin/product-list";
    }


    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", accountRepository.findAll());
        return "admin/user-list";
    }
}