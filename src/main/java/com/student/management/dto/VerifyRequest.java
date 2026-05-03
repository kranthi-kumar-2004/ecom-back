package com.student.management.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyRequest {

    private String razorpay_order_id;
    private String razorpay_payment_id;
    private String razorpay_signature;
    private List<OrderItemDTO> items;
}