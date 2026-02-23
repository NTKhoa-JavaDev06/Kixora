package poly.com.asm_kixora.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.com.asm_kixora.entity.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartIplm implements ShoppingCartService {

    @Autowired CartRepository cartRepository;
    @Autowired CartItemsRepository cartItemsRepository;
    @Autowired ProductVariantRepository variantRepo;


    private Cart getOrCreateCart(Integer userId) {
        Cart cart = cartRepository.findByAccount_Id(userId);
        if (cart == null) {
            cart = new Cart();

            cart.setUserId(userId);
            cart.setCreatedDate(LocalDateTime.now());
            cart = cartRepository.save(cart);
        }
        return cart;
    }

    @Override
    public void add(Integer userId, Integer variantId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        CartItems existItem = cartItemsRepository.findByCart_IdAndProductVariant_Id(cart.getId(), variantId);

        if (existItem != null) {
            existItem.setQuantity(existItem.getQuantity() + quantity);
            cartItemsRepository.save(existItem);
        } else {
            ProductVariants pv = variantRepo.findById(variantId).orElseThrow();
            CartItems newItem = new CartItems();
            newItem.setCart(cart);
            newItem.setProductVariant(pv);
            newItem.setQuantity(quantity);
            newItem.setAddedDate(LocalDateTime.now());
            cartItemsRepository.save(newItem);
        }
    }

    @Override
    public void remove(Integer userId, Integer variantId) {
        Cart cart = cartRepository.findByAccount_Id(userId);
        if (cart != null) {
            CartItems item = cartItemsRepository.findByCart_IdAndProductVariant_Id(cart.getId(), variantId);
            if (item != null) cartItemsRepository.delete(item);
        }
    }

    @Override
    public void update(Integer userId, Integer variantId, int qty) {
        Cart cart = cartRepository.findByAccount_Id(userId);
        if (cart != null) {
            CartItems item = cartItemsRepository.findByCart_IdAndProductVariant_Id(cart.getId(), variantId);
            if (item != null) {
                item.setQuantity(qty);
                cartItemsRepository.save(item);
            }
        }
    }

    @Override
    public void clear(Integer userId) {
        Cart cart = cartRepository.findByAccount_Id(userId);
        if (cart != null) {
            List<CartItems> items = cartItemsRepository.findByCart_Id(cart.getId());
            cartItemsRepository.deleteAll(items); // Xóa sạch bảng CartItems của khách này
        }
    }

    @Override
    public List<CartItem> getItems(Integer userId) {
        Cart cart = cartRepository.findByAccount_Id(userId);
        List<CartItem> result = new ArrayList<>();
        if (cart == null) return result;

        List<CartItems> dbItems = cartItemsRepository.findByCart_Id(cart.getId());

   for (CartItems dbItem : dbItems) {
            ProductVariants pv = dbItem.getProductVariant();
            CartItem dto = new CartItem();
            dto.setVariantId(pv.getId());
            dto.setProductId(pv.getProduct().getId());
            dto.setName(pv.getProduct().getName());
            dto.setImage(pv.getProduct().getImage());

            dto.setPrice(pv.getProduct().getPrice().doubleValue());
            dto.setSize(pv.getSize());
            dto.setColor(pv.getColor());
            dto.setQuantity(dbItem.getQuantity());
               dto.setStockQuantity(pv.getQuantity());
            result.add(dto);

        }
        return result;
    }

    @Override
    public int getCount(Integer userId) {
        return getItems(userId).size();
    }

    @Override
    public double getAmount(Integer userId) {
        return getItems(userId).stream().mapToDouble(CartItem::getAmount).sum();
    }

}