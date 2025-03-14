package com.horizon.ebooklibrary.ebooklibrarybackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * Controller class to test JWT authentication.
 * This endpoint is protected and requires a valid JWT token.
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(HelloController.class);

    /**
     * Protected endpoint. Only accessible with a valid JWT token.
     * @return a greeting message if authentication was successful.
     */
    @GetMapping("/hello")
    @PreAuthorize("isAuthenticated()") // Ensures only authenticated users can access
    public ResponseEntity<Map<String, String>> sayHello() {
        logger.info("User successfully accessed /api/hello");
        return ResponseEntity.ok(Map.of("message", "Hello! You have accessed a protected API."));
    }


}
