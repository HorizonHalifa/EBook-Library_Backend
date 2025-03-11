package com.horizon.ebooklibrary.ebooklibrarybackend.controller;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.User;
import com.horizon.ebooklibrary.ebooklibrarybackend.security.JwtUtils;
import com.horizon.ebooklibrary.ebooklibrarybackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import lombok.RequiredArgsConstructor;

/**
 * Controller that handles user authentication requests.
 * Allows users to sign up.
 * Allows users to log in and receive a JWT token.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    /**
     * User registration:
     * Accepts email and password.
     * Saves the user after encryption the password.
     * Returns a success message
     * @param user User object containing email and password.
     * @return HTTP 200 OK with success message.
     */
    // SIGNUP Endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * User login:
     * Validates user credentials.
     * If correct, returns a JWT token for authentication.
     * If incorrect, returns 401 Unauthorized.
     * @param user User object containing email and password.
     * @return HTTP 200 OK with JWT token if successful, 401 Unauthorized if failed.
     */
    // LOGIN Endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        // Call authenticate() method, which returns the JWT token if successful.
        String token = userService.authenticate(user.getEmail(), user.getPassword());

        if(token != null) { // if authentication was successful
            return ResponseEntity.ok(Map.of("Token", token)); // Return JWT token
        } else { // if authentication failed
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
}