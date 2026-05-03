package com.student.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.management.Repository.ProductRepository;
import com.student.management.Repository.UserRepository;
import com.student.management.Repository.WishlistRepository;
import com.student.management.dto.WishlistRequest;
import com.student.management.enitity.Products;
import com.student.management.enitity.User;
import com.student.management.enitity.Wishlist;

import java.util.List;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;
      public String add(Integer userId, Long productId) {

        if (wishlistRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
            return "Already exists";
        }
        Long userId1 = 1L;

        User user = userRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Products product = productRepository.findById(productId).orElseThrow();

        Wishlist w = new Wishlist();
        w.setUser(user);
        w.setProduct(product);

        wishlistRepository.save(w);
        return "Added";
    }
      public List<WishlistRequest> get(Long userId) {

        return wishlistRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(w -> new WishlistRequest(
                        w.getProduct().getId(),
                        w.getProduct().getName(),
                        w.getProduct().getPrice(),
                        w.getProduct().getImage()
                )).toList();
    }
}
