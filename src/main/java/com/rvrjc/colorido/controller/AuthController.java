package com.rvrjc.colorido.controller;

import com.rvrjc.colorido.config.JwtUtils;
import com.rvrjc.colorido.dto.AuthResponse;
import com.rvrjc.colorido.dto.LoginRequest;
import com.rvrjc.colorido.entity.Admin;
import com.rvrjc.colorido.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and password are required.");
        }

        Optional<Admin> adminOpt = adminRepository.findByUsername(request.getUsername());
        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }

        Admin admin = adminOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }

        String token = jwtUtils.generateToken(admin.getUsername(), admin.getRole());
        return ResponseEntity.ok(new AuthResponse(token, admin.getUsername(), admin.getFullName(), admin.getRole()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated.");
        }
        Optional<Admin> adminOpt = adminRepository.findByUsername(authentication.getName());
        if (adminOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Admin admin = adminOpt.get();
        return ResponseEntity.ok(new AuthResponse("", admin.getUsername(), admin.getFullName(), admin.getRole()));
    }
}
