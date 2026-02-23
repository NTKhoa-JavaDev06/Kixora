package poly.com.asm_kixora.Order;

import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.com.asm_kixora.Product.ShoppingCartService;
import poly.com.asm_kixora.Voucher.UserVouchersRepository;
import poly.com.asm_kixora.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class OrderCtrl {

    @Autowired ShoppingCartService cartService;
    @Autowired OrdersRepository ordersRepository;
    @Autowired OrderDetailsRepository orderDetailsRepository;
    @Autowired UserVouchersRepository userVouchersRepository;

    @PostMapping("/order/place")
    public String placeOrder(
            @RequestParam("receiverName") String receiverName,
            @RequestParam("receiverPhone") String receiverPhone,
            @RequestParam("shippingAddress") String shippingAddress,
            @RequestParam(value = "notes", required = false) String notes,
            @RequestParam(value = "appliedVoucherCode", required = false) String appliedVoucherCode,
            @RequestParam(value = "shippingFee", required = false, defaultValue = "0") BigDecimal shippingFee,
            @RequestParam(value = "paymentMethod", required = false) String paymentMethod,
            HttpSession session, RedirectAttributes redirect, Model model) {

        // 1. Kiểm tra User
        Accounts user = (Accounts) session.getAttribute("user");
        if (user == null) return "redirect:/Login/login";

        // 2. Kiểm tra Giỏ hàng
        List<CartItem> cartItems = cartService.getItems(user.getId());
        if (cartItems.isEmpty()) {
            redirect.addFlashAttribute("error", "Giỏ hàng trống!");
            return "redirect:/cart/view";
        }

        // 3. Tính tiền
        BigDecimal rawTotal = BigDecimal.valueOf(cartService.getAmount(user.getId()));
        BigDecimal discountAmount = BigDecimal.ZERO;

        // 4. Voucher
        if (appliedVoucherCode != null && !appliedVoucherCode.isEmpty()) {
            UserVouchers uv = userVouchersRepository.takevoucherstouse(user.getId(), appliedVoucherCode);
            if (uv != null && (uv.getIsUsed() == null || !uv.getIsUsed())) {
                discountAmount = uv.getVoucher().getDiscountAmount();
                uv.setIsUsed(true);
                userVouchersRepository.save(uv);
            }
        }

        BigDecimal finalTotal = rawTotal.add(shippingFee).subtract(discountAmount);
        if (finalTotal.compareTo(BigDecimal.ZERO) < 0) finalTotal = BigDecimal.ZERO;

        // 5. Lưu Order (Để lấy ID tự tăng)
        Orders order = new Orders();
        order.setUserId(user.getId());
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(finalTotal);
        order.setShippingFee(shippingFee);
        order.setStatus("Pending");

        boolean isQR = "QR".equals(paymentMethod);
        order.setPaymentMethod(isQR ? "Chuyển khoản QR" : "Thanh toán khi nhận hàng (COD)");
        order.setPaymentStatus("Unpaid");

        String fullAddress = receiverName + " - " + receiverPhone + " - " + shippingAddress;
        if (notes != null && !notes.isEmpty()) fullAddress += " (Ghi chú: " + notes + ")";
        order.setShippingAddress(fullAddress);

        Orders savedOrder = ordersRepository.save(order);

        // 6. Lưu OrderDetails (QUAN TRỌNG: Sửa lỗi NULL ProductVariantId)
        for (CartItem item : cartItems) {
            OrderDetails detail = new OrderDetails();
            detail.setOrderId(savedOrder.getId());

            // TẠO OBJECT VARIANT VÀ SET ID VÀO (Đây là cách fix lỗi 500 của bạn)
            ProductVariants variant = new ProductVariants();
            variant.setId(item.getVariantId());
            detail.setProductVariant(variant); // Gán nguyên object chứa ID vào

            detail.setQuantity(item.getQuantity());
            detail.setUnitPrice(BigDecimal.valueOf(item.getPrice()));
            detail.setDiscount(BigDecimal.ZERO);

            orderDetailsRepository.save(detail);
        }

        // 7. Dọn giỏ
        cartService.clear(user.getId());

        // 8. Chuyển hướng
        if (isQR) {
            String bankId = "tpb";
            String accountNo = "0797762879";
            String accountName = "Nguyen Tuan Khoa";
            String content = "KIXORA" + savedOrder.getId();

            String qrUrl = String.format(
                    "https://img.vietqr.io/image/%s-%s-compact2.png?amount=%d&addInfo=%s&accountName=%s",
                    bankId, accountNo, savedOrder.getTotalAmount().longValue(), content, accountName
            );

            model.addAttribute("qrUrl", qrUrl);
            model.addAttribute("order", savedOrder);
            return "cart/pay_qr";
        }

        redirect.addFlashAttribute("success", "Đặt hàng thành công!");
        return "redirect:/home";
    }
}