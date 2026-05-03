package com.student.management.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.management.Repository.OrderItemRepository;
import com.student.management.Repository.OrderRepository;
import com.student.management.Repository.ProductRepository;
import com.student.management.enitity.OrderItem;
import com.student.management.enitity.Orders;
import com.student.management.enitity.Products;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderItemRepository itemRepo;

    @Autowired
    private ProductRepository productRepo;

   public List<Map<String, Object>> getUserOrders(Long userId) {

    List<Orders> orders = orderRepo.findByUserId(userId);
    List<Map<String, Object>> result = new ArrayList<>();

    for (Orders order : orders) {

        Map<String, Object> orderMap = new HashMap<>();

        // ✅ FIXED FIELD NAMES
        orderMap.put("order_id", order.getOrderId());
        orderMap.put("total_price", order.getTotalPrice());
        orderMap.put("delivery_status", order.getDeliveryStatus());
        orderMap.put("order_date", order.getOrderDate());

        // ✅ NEW FIELDS (IMPORTANT)
        orderMap.put("payment_mode", order.getPaymentMode());
        orderMap.put("payment_status", order.getPaymentStatus());
        orderMap.put("address_id", order.getAddressId());

        List<OrderItem> items = itemRepo.findByOrderId(order.getId());
        List<Map<String, Object>> itemList = new ArrayList<>();

        for (OrderItem item : items) {

            Products product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("productName", product.getName());
            itemMap.put("quantity", item.getQuantity());
            itemMap.put("productImage", product.getImage());

            itemList.add(itemMap);
        }

        orderMap.put("items", itemList);
        result.add(orderMap);
    }

    return result;
}
}