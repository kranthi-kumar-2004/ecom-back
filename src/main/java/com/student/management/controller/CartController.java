package com.student.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.student.management.Repository.CartRepository;
import com.student.management.dto.CartRequest;
import com.student.management.security.JwtUtil;
import com.student.management.services.CartService;
import com.student.management.enitity.Cart;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository repo;
      private Long getUserId(String token) {
        return JwtUtil.extractUserId(token.replace("Bearer ", ""));
    }
        @GetMapping
    public ResponseEntity<?> getCartItems(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            Long userId = getUserId(authHeader);

            List<Cart> items = cartService.getCartItems(userId);

            return ResponseEntity.ok(items);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(
            @RequestHeader("Authorization") String token
    ) {

        Long userId = getUserId(token);

        repo.deleteCartByUserId(userId); 


        return ResponseEntity.ok("Cart cleared");
    }

    @PostMapping
    public ResponseEntity<?> updateCart(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CartRequest request
    ) {
        try {
            Long userId = getUserId(authHeader);

            int qty = cartService.updateCart(
                    userId,
                    request.getProductId(),
                    request.getQuantity()
            );

            return ResponseEntity.ok(Map.of(
                    "productId", request.getProductId(),
                    "quantity", qty
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", e.getMessage()));
        }
    }
        @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteItem(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long productId
    ) {
        try {
            Long userId = getUserId(authHeader);

            cartService.updateCart(userId, productId, 0);

            return ResponseEntity.ok(Map.of("message", "Deleted ✅"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}