package com.horizon.ebooklibrary.ebooklibrarybackend.controller;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.User;
import com.horizon.ebooklibrary.ebooklibrarybackend.repository.UserRepository;
import com.horizon.ebooklibrary.ebooklibrarybackend.security.JwtUtils;
import com.horizon.ebooklibrary.ebooklibrarybackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import io.jsonwebtoken.ExpiredJwtException;
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
            User existingUser = UserService.findByEmail(user.getEmail());
            String refreshToken = jwtUtils.generateRefreshToken(existingUser);
            return ResponseEntity.ok(Map.of(
                    "access token", token,
                    "refresh token", refreshToken
            )); // Return JWT token
        } else { // if authentication failed
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken =request.get("refreshToken");

        try {
            String email = jwtUtils.getEmailFromToken(refreshToken);
            User user = userService.findByEmail(email);
            String newAccessToken = jwtUtils.generateToken(user);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Refresh token has expired. Please log in again."));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid refresh token."));
        }
    }
}