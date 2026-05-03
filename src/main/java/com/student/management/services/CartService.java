package com.student.management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.management.Repository.CartRepository;
import com.student.management.enitity.Cart;

@Service
public class CartService {

    @Autowired
    private CartRepository repo;
    public List<Cart> getCartItems(Long userId) {
    return repo.findByUserId(userId.intValue());
}
    public int updateCart(Long userId, Long productId, int quantity) {

        System.out.println("UserId: " + userId);
        System.out.println("ProductId: " + productId);
        System.out.println("Quantity: " + quantity);

        if (userId == null) {
            throw new RuntimeException("UserId is NULL ❌");
        }

        if (productId == null) {
            throw new RuntimeException("ProductId is NULL ❌");
        }

        Integer uid = userId.intValue(); // 🔥 convert
          if (quantity == 0) {
            repo.findByUserIdAndProductId(uid, productId)
                    .ifPresent(repo::delete);
            return 0;
        }
          Cart cart = repo.findByUserIdAndProductId(uid, productId)
                .orElse(new Cart());

        cart.setUserId(userId);
        cart.setProductId(productId);
        cart.setQuantity(quantity);

        repo.save(cart);

        return cart.getQuantity();
    }
}