package com.student.management.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.student.management.Repository.LoginResponse;
import com.student.management.Repository.UserRepository;
import com.student.management.dto.LoginRequest;
import com.student.management.dto.OtpStorage;
import com.student.management.dto.RegisterRequest;
import com.student.management.enitity.User;
import com.student.management.security.JwtUtil;
import com.student.management.services.EmailService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        // 🔥 Check email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body("Email already exists");
        }

        // 🔥 Basic validation
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Name is required");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = JwtUtil.generateToken(user.getId());

        return new LoginResponse(token);
    }
    @PostMapping("/send-otp")
public ResponseEntity<?> sendOtp(
        @RequestBody Map<String, String> request
) {

    try {

        String email = request.get("email");

        System.out.println(email);

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body("Email not found");
        }

        String otp =
                String.valueOf((int)(Math.random() * 900000) + 100000);

        System.out.println("OTP: " + otp);

        OtpStorage.otpMap.put(email, otp);

        emailService.sendOtp(email, otp);

        return ResponseEntity.ok("OTP sent successfully");

    } catch (Exception e) {

        e.printStackTrace();

        return ResponseEntity.internalServerError()
                .body(e.getMessage());
    }
}
@PostMapping("/reset-password")
public ResponseEntity<String> resetPassword(
        @RequestBody Map<String, String> request
) {

    String email = request.get("email");
    String otp = request.get("otp");
    String newPassword = request.get("newPassword");

    String storedOtp = OtpStorage.otpMap.get(email);

    if (storedOtp == null ||
            !storedOtp.equals(otp)) {

        return ResponseEntity.badRequest()
                .body("Invalid OTP");
    }

    User user = userRepository.findByEmail(email)
            .orElseThrow();

    user.setPassword(
            passwordEncoder.encode(newPassword)
    );

    userRepository.save(user);

    OtpStorage.otpMap.remove(email);

    return ResponseEntity.ok(
            "Password updated successfully"
    );
}
}