package com.student.management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.student.management.Repository.AddressRepository;
import com.student.management.Repository.OrderRepository;
import com.student.management.Repository.UserRepository;
import com.student.management.enitity.Address;
import com.student.management.enitity.Orders;
import com.student.management.enitity.User;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AdminController {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private UserRepository userRepo;
      @GetMapping("/orders")
    public List<Orders> getAllOrders() {
        return orderRepo.findAll();
    }
    @Autowired
    private AddressRepository addressRepo;
@GetMapping("/admin/addresses")
public List<Address> getAllAddresses() {
    return addressRepo.findAll();
}
   @PutMapping("/orders/{id}")
public Orders updateOrder(@PathVariable Long id, @RequestBody Map<String, String> body) {

    System.out.println("Incoming body: " + body);

    Orders order = orderRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    String status = body.get("deliveryStatus");

    System.out.println("Status received: " + status);

    order.setDeliveryStatus(status);

    return orderRepo.save(order);
}
      @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}