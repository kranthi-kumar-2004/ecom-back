package com.student.management.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.student.management.enitity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
     List<OrderItem> findByOrderId(Long orderId);
}