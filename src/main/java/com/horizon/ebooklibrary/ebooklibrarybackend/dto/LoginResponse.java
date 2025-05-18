package com.horizon.ebooklibrary.ebooklibrarybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO returned to the frontend after successful login, containing JWT tokens and the user's role
 */
@SuppressWarnings("unused")
@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String role;
}
