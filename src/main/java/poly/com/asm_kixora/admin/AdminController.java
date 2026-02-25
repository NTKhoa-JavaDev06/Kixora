package poly.com.asm_kixora.admin;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.com.asm_kixora.Order.OrderDetailsRepository;
import poly.com.asm_kixora.Order.OrdersRepository;
import poly.com.asm_kixora.Product.AccountRepository;
import poly.com.asm_kixora.Product.BrandRepository;
import poly.com.asm_kixora.Product.ProductRepository;
import poly.com.asm_kixora.Product.ProductVariantRepository;
import poly.com.asm_kixora.entity.*; // Chỗ này tui import tất cả Entity luôn cho gọn
import poly.com.asm_kixora.Catagory.Rep.CatagoryRes;

import java.time.LocalDateTime; // ĐÃ SỬA: Dùng LocalDateTime theo đúng Entity của bro
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private Cloudinary cloudinary;
    @Autowired ProductRepository productRepo;
    @Autowired OrdersRepository orderRepo;
    @Autowired OrderDetailsRepository orderDetailsRepo;
    @Autowired AccountRepository accountRepository;
    @Autowired ProductVariantRepository variantRepo;
    @Autowired CatagoryRes catagoryRes;
    @Autowired BrandRepository brandRepo;

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

    // 2. CHI TIẾT ĐƠN HÀNG
    @GetMapping("/orders/detail/{id}")
    public String orderDetail(@PathVariable("id") Integer id, Model model) {
        Orders order = orderRepo.findById(id).orElse(null);
        if (order == null) return "redirect:/admin/orders";

        List<OrderDetails> details = orderDetailsRepo.findByOrderId(id);
        model.addAttribute("order", order);
        model.addAttribute("details", details);

        return "admin/order-detail";
    }

    // 3. DANH SÁCH ĐƠN HÀNG
    @GetMapping("/orders")
    public String listOrders(Model model) {
        List<Orders> allOrders = orderRepo.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
        model.addAttribute("orders", allOrders);
        return "admin/order-list";
    }

    // 4. CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG
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

    // 5. DANH SÁCH NGƯỜI DÙNG
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", accountRepository.findAll());
        return "admin/user-list";
    }

    // 6. DANH SÁCH SẢN PHẨM
    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("categories", catagoryRes.findAll());
        model.addAttribute("brands", brandRepo.findAll());
        return "admin/product-list";
    }

    // 7. THÊM SẢN PHẨM & PHÂN LOẠI (Lưu cùng lúc 2 bảng)
    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Products product,
                             @RequestParam("categoryId") Integer categoryId, // Lấy ID danh mục từ form HTML
                             @RequestParam("brandId") Integer brandId,       // Lấy ID thương hiệu từ form HTML
                             @RequestParam("imageFile") MultipartFile file,
                             @RequestParam("size") String size,
                             @RequestParam("color") String color,
                             @RequestParam("quantity") Integer quantity) {
        try {
            // 1. Upload ảnh lên mây (Cloudinary)
            if (!file.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = uploadResult.get("secure_url").toString();
                product.setImage(imageUrl);
            }

            // 2. Ép ID Category và Brand thành Object để JPA không bị lỗi
            Categories cat = new Categories();
            cat.setId(categoryId);
            product.setCategory(cat);

            Brand brand = new Brand();
            brand.setId(brandId);
            product.setBrand(brand);

            // 3. Set Ngày tạo (Dùng LocalDateTime)
            product.setCreatedDate(LocalDateTime.now());

            // 4. Lưu vào bảng Products trước để nó sinh ra ID
            Products savedProduct = productRepo.save(product);

            // 5. Tạo mới và lưu vào bảng ProductVariants
            ProductVariants variant = new ProductVariants();

            // Ép Object Products vào Variant (Vì trong Entity bro xài @ManyToOne private Products product;)
            variant.setProduct(savedProduct);

            variant.setSize(size);
            variant.setColor(color);
            variant.setQuantity(quantity);
            variant.setCreatedDate(LocalDateTime.now());

            variantRepo.save(variant);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/products";
    }
}