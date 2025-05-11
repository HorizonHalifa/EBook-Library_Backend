package com.horizon.ebooklibrary.ebooklibrarybackend.controller;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.User;
import com.horizon.ebooklibrary.ebooklibrarybackend.security.JwtUtils;
import com.horizon.ebooklibrary.ebooklibrarybackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

/**
 * Controller that handles user authentication requests.
 * - Sign up new users.
 * - Log in users and return JWT tokens
 * - Refresh access tokens using refresh tokens.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    /**
     * User registration endpoint.
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
     * User login endpoint.
     * Validates credentials, returns access and refresh tokens if successful,
     * otherwise shows 401 Unauthorized.
     * @param user User object containing email and password.
     * @return JWT tokens or 401 Unauthorized.
     */
    // LOGIN Endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        // Call authenticate() method, which returns the JWT token if successful.
        String token = userService.authenticate(user.getEmail(), user.getPassword());

        if(token != null) { // if authentication was successful
            Optional<User> optionalUser = userService.findByEmail(user.getEmail());

            if(optionalUser.isPresent()) {
                User existingUser = optionalUser.get();
                String refreshToken= jwtUtils.generateRefreshToken(existingUser);

                return ResponseEntity.ok(Map.of(
                        "access token", token,
                        "refresh token", refreshToken
                ));
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
            }

        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    /**
     * Endpoint to refresh the access token using a refresh token.
     * @param request JSON body with "refreshToken" field.
     * @return New access token or 401 error.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken =request.get("refreshToken");

        try {
            String email = jwtUtils.getEmailFromToken(refreshToken);
            Optional<User> optionalUser = userService.findByEmail(email);

            if(optionalUser.isPresent()) {
                User user = optionalUser.get();
                String newAccessToken = jwtUtils.generateToken(user);
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Refresh token has expired. Please log in again."));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid refresh token."));
        }
    }
}