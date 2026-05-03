package com.student.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.student.management.Repository.UserRepository;
import com.student.management.dto.ProfileResponse;
import com.student.management.enitity.User;
import com.student.management.security.JwtUtil;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
      @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {

        try {
              String token = authHeader.replace("Bearer ", "");
              Long userId = JwtUtil.extractUserId(token);
              User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
              ProfileResponse response = new ProfileResponse(
                    user.getName(),
                    user.getEmail()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }
}