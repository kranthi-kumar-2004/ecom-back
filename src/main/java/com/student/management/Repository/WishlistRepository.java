package com.student.management.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.student.management.enitity.Wishlist;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
     List<Wishlist> findByUser_IdOrderByCreatedAtDesc(Long userId);
     @Transactional
    @Modifying
    @Query("DELETE FROM Wishlist w WHERE w.user.id = :userId AND w.product.id = :productId")
    int deleteWishlist(@Param("userId") Long userId,
                       @Param("productId") Long productId);

    List<Wishlist> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Wishlist> findByUserIdAndProductId(Integer userId, Long productId);
}