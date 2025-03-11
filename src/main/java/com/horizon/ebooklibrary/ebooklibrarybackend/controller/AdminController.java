package com.horizon.ebooklibrary.ebooklibrarybackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import lombok.RequiredArgsConstructor;

/**
 * Controller to handle admin only requests
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    /**
     * Admin Dashboard (Only accessible by users with ADMIN role).
     * @return Success message
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // only admins can access this endpoint
    public ResponseEntity<Map<String, String>> accessAdminDashboard() {
        return ResponseEntity.ok(Map.of("message", "Welcome admin! You have access to the admin dashboard."));
    }
}
