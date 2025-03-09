package com.horizon.ebooklibrary.ebooklibrarybackend.controller;

import com.horizon.ebooklibrary.ebooklibrarybackend.model.User;
import com.horizon.ebooklibrary.ebooklibrarybackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> request) {
        try {
            User newUser = userService.registerUser(request.get("email"), request.get("password"));
            return ResponseEntity.ok(Map.of("message", "User registered successfully", "userId", newUser.getId()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        boolean isAuthenticated = userService.authenticateUser(request.get("email"), request.get("password"));
        if (!isAuthenticated) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid email or password"));
        }
        return ResponseEntity.ok(Map.of("message", "Login successful"));
    }
}