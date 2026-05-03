package com.student.management.enitity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", unique = true)
    private String orderId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(name = "payment_mode")
    private String paymentMode;

    // 🔥 NEW: PAYMENT STATUS
    @Column(name = "payment_status")
    private String paymentStatus = "PENDING";

    // 🔥 EXISTING (KEEP)
    @Column(name = "delivery_status")
    private String deliveryStatus = "PENDING";

    @Column(name = "order_date")
    private LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;

    @Column(name = "razorpay_order_id")
    private String razorpayOrderId;

    // 🔥 NEW: ADDRESS ID
    @Column(name = "address_id")
    private Long addressId;
}