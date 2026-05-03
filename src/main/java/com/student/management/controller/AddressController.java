
package com.student.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.student.management.Repository.AddressRepository;
import com.student.management.enitity.Address;

@RestController
@RequestMapping("/api/address")
@CrossOrigin
public class AddressController{

    @Autowired
    private AddressRepository addressRepo;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable Long id) {

        Address address = addressRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        return ResponseEntity.ok(address);
    }
}