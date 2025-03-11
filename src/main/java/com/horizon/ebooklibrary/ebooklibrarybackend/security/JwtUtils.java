package com.horizon.ebooklibrary.ebooklibrarybackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/*
 * Utility class for handling JWT operations such as token generation and validation.
 */
@Component
public class JwtUtils {
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    // HMAC with SHA-256 is used for signing JWTs
    private static final String SECRET = "+pu/Q8KgBbnGUJ/MKA/meHBAAekvMt+Y+CzD+GHI/fw=";
    private final Key secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET));

    /**
     * Generates a JWT token for the given user email.
     *
     * @param email the email to include in the token.
     * @param role
     * @return A signed JWT token.
     */
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email) // Set user email as subject
                .addClaims(Map.of("role", role)) // include role in JWT payload
                .setIssuedAt(new Date()) // When token was issued
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 1 day expiry time
                .signWith(secretKey, SignatureAlgorithm.HS256) // Sign with secret key
                .compact();
    }

    /**
     * Extracts the email (subject) from the token.
     * @param token the JWT token.
     * @return The email stored in the token.
     */
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Extracts the user role from the token.
     * @param token the JWT token.
     * @return The role stored in the token.
     */
    public String getRoleFromToken(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    /**
     * Validates the given JWT token.
     * @param token the JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

}
