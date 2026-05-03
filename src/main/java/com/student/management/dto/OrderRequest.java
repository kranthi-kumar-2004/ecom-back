package com.student.management.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {

    private List<CartItem> items;

    // 🔥 ADD THIS (IMPORTANT)
    private String paymentMethod;
    private Long addressId;
}