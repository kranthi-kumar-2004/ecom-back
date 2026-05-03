package com.student.management.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import com.student.management.enitity.*;
import com.student.management.Repository.*;
import com.student.management.security.JwtUtil;
import com.student.management.services.OrderService;
import com.student.management.dto.*;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin
public class CheckoutController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private OrderItemRepository itemRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private OrderService orderService;

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    // ================= ADDRESS =================

    @PutMapping("/address")
    public ResponseEntity<?> saveAddress(@RequestBody Address address,
            @RequestHeader("Authorization") String token) {

        Long userId = JwtUtil.extractUserId(token.replace("Bearer ", ""));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        address.setUser(user);
        Address saved = addressRepo.save(address);

        return ResponseEntity.ok(saved); // ✅ FIXED JSON
    }

    @GetMapping("/address")
    public ResponseEntity<?> getAddresses(@RequestHeader("Authorization") String token) {

        Long userId = JwtUtil.extractUserId(token.replace("Bearer ", ""));
        return ResponseEntity.ok(addressRepo.findByUser_Id(userId));
    }

    // ================= CREATE ORDER =================

    @PostMapping("/payment/create-order")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request,
            @RequestHeader("Authorization") String token) throws Exception {

        double total = 0;

        for (CartItem item : request.getItems()) {

            Products product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (item.getQuantity() <= 0) {
                throw new RuntimeException("Invalid quantity");
            }

            // ✅ STOCK VALIDATION
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock");
            }

            double price = product.getDiscount_price() > 0
        ? product.getDiscount_price()
        : product.getPrice();

total += price * item.getQuantity();
        }

        Long userId = JwtUtil.extractUserId(token.replace("Bearer ", ""));

        Orders order = new Orders();
        order.setUserId(userId);
        order.setTotalPrice(total);
        order.setPaymentMode(request.getPaymentMethod());

        // 🔥 NEW
        order.setPaymentStatus("PENDING");
        order.setDeliveryStatus("PENDING");

        // 🔥 ADDRESS ID FROM FRONTEND
        order.setAddressId(request.getAddressId());

        Orders saved = orderRepo.save(order);

        // ================= COD FLOW =================
        if ("COD".equalsIgnoreCase(request.getPaymentMethod())) {

            for (CartItem item : request.getItems()) {

                Products product = productRepo.findById(item.getProductId()).orElseThrow();

                product.setStock(product.getStock() - item.getQuantity());
                productRepo.save(product);

                OrderItem oi = new OrderItem();
                oi.setOrderId(saved.getId());
                oi.setProductId(product.getId());
                oi.setQuantity(item.getQuantity());
                oi.setPrice(product.getPrice());

                itemRepo.save(oi);
            }

            String dateTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            saved.setOrderId("ORD-" + dateTime + "-" + saved.getId());
            saved.setPaymentStatus("SUCCESS"); // 🔥 COD = success
            orderRepo.save(saved);

            return ResponseEntity.ok(Map.of("message", "COD order placed"));
        }

        // ================= ONLINE FLOW =================
        RazorpayClient client = new RazorpayClient(key, secret);

        JSONObject options = new JSONObject();
        options.put("amount", (int) (total * 100));
        options.put("currency", "INR");
        options.put("receipt", "order_" + saved.getId());

        Order rzpOrder = client.orders.create(options);

        saved.setRazorpayOrderId(rzpOrder.get("id"));
        orderRepo.save(saved);

        Map<String, Object> res = new HashMap<>();
        res.put("id", rzpOrder.get("id"));
        res.put("amount", rzpOrder.get("amount"));

        return ResponseEntity.ok(res);
    }

    // ================= VERIFY PAYMENT =================

    @PostMapping("/payment/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody VerifyRequest request) throws Exception {

        String razorpayOrderId = request.getRazorpay_order_id();
        String paymentId = request.getRazorpay_payment_id();
        String signature = request.getRazorpay_signature();

        String payload = razorpayOrderId + "|" + paymentId;
        String generated = hmacSHA256(payload, secret);

        if (!generated.equals(signature)) {
            return ResponseEntity.status(400).body("Invalid payment signature");
        }

        RazorpayClient client = new RazorpayClient(key, secret);
        com.razorpay.Payment payment = client.payments.fetch(paymentId);

        if (!payment.get("status").toString().equals("captured")) {
            return ResponseEntity.status(400).body("Payment not captured");
        }

        String method = payment.get("method");

        Orders order = orderRepo.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setRazorpayPaymentId(paymentId);
        order.setPaymentMode(method);

        // 🔥 NEW
        order.setPaymentStatus("SUCCESS");

        orderRepo.save(order);

        // 🔥 SAVE ITEMS + STOCK
        for (OrderItemDTO item : request.getItems()) {

            Products product = productRepo.findById(item.getProductId())
                    .orElseThrow();

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock");
            }

            product.setStock(product.getStock() - item.getQuantity());
            productRepo.save(product);

            OrderItem oi = new OrderItem();
            oi.setOrderId(order.getId());
            oi.setProductId(product.getId());
            oi.setQuantity(item.getQuantity());
            oi.setPrice(product.getPrice());

            itemRepo.save(oi);
        }

        String dateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        order.setOrderId("ORD-" + dateTime + "-" + order.getId());
        orderRepo.save(order);

        return ResponseEntity.ok("Payment successful & order placed");
    }

    public String hmacSHA256(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));

        byte[] raw = mac.doFinal(data.getBytes());

        StringBuilder hex = new StringBuilder();
        for (byte b : raw) {
            String s = Integer.toHexString(0xff & b);
            if (s.length() == 1)
                hex.append('0');
            hex.append(s);
        }
        return hex.toString();
    }

    @PostMapping("/payment/fail")
    public ResponseEntity<?> markFailed(@RequestBody Map<String, String> req) {

        Orders order = orderRepo
                .findByRazorpayOrderId(req.get("razorpayOrderId"))
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setPaymentStatus("FAILED"); // 🔥 important
        orderRepo.save(order);

        return ResponseEntity.ok("Order marked as FAILED");
    }
    // ================= MY ORDERS =================

    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(@RequestHeader("Authorization") String token) {

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        Long userId = JwtUtil.extractUserId(jwt);

        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }
}