package com.horizon.ebooklibrary.ebooklibrarybackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller to handle admin only requests
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    /**
     * TODO: example admin only endpoint.
     * @return Success message
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')") // only admins can access this endpoint
    public ResponseEntity<Map<String, String>> accessAdminDashboard() {
        return ResponseEntity.ok(Map.of("message", "Welcome admin! You have access toi the admin dashboard."));
    }
}
