package com.student.management.dto;

import lombok.Data;

@Data
public class CartItem {
    private Long productId;
    private int quantity;
}