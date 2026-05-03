package com.student.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.student.management.Repository.WishlistRepository;

import com.student.management.dto.WishlistRequest;
import com.student.management.enitity.Products;
import com.student.management.enitity.User;
import com.student.management.enitity.Wishlist;
import com.student.management.security.JwtUtil;

import java.util.List;
@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistRepository wishlistRepository;
    @GetMapping
public ResponseEntity<?> getWishlist(
        @RequestHeader("Authorization") String authHeader
) {
    try {
        Long userId = JwtUtil.extractUserId(authHeader);

        List<WishlistRequest> wishlist = wishlistRepository
                .findByUser_IdOrderByCreatedAtDesc(userId)
                .stream()
                .map(w -> {
                    Products p = w.getProduct();
                    return new WishlistRequest(
                            p.getId(),
                            p.getName(),
                            p.getPrice(),
                            p.getImage()
                    );
                }).toList();

        return ResponseEntity.ok(wishlist);

    } catch (Exception e) {
        e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Access Denied");
    }
}
@PostMapping("/add/{productId}")
public ResponseEntity<?> addToWishlist(
        @PathVariable Long productId,
        @RequestHeader("Authorization") String authHeader
) {
    try {
        String token = authHeader.replace("Bearer ", "").trim();
        Long userId = JwtUtil.extractUserId(token);

        User user = new User();
        user.setId(userId);

        Products product = new Products();
        product.setId(productId);

        Wishlist wishlist = new Wishlist(user, product);

        wishlistRepository.save(wishlist);

        return ResponseEntity.ok("Added to wishlist");

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
    }
}
    @DeleteMapping("/remove/{productId}")
public ResponseEntity<?> removeWishlistItem(
        @PathVariable Long productId,
        @RequestHeader("Authorization") String authHeader
) {
    try {
        Long userId = JwtUtil.extractUserId(authHeader);

        int deleted = wishlistRepository.deleteWishlist(userId, productId);

        if (deleted == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Item not found");
        }

        return ResponseEntity.ok("Removed");

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
    }
}
}