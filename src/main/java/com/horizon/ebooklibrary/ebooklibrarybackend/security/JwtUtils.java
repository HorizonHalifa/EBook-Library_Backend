package com.horizon.ebooklibrary.ebooklibrarybackend.security;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.User;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

/*
 * Utility class for handling JWT operations such as token generation and validation.
 */
@SuppressWarnings({"LombokGetterMayBeUsed", "unused"})
@Component
public class JwtUtils {

    // Access tokens expire in 15 minutes, refresh tokens expire in 2 hours.
    private static final long EXPIRATION_TIME_15 = 900000; // 15 minutes expiry in ms value
    private static final long EXPIRATION_TIME_2HR = 7200000; // 2 hours expiry in ms value

    // Secret ket used to sign tokens
    private final String SECRET = JwtSecretLoader.loadJwtSecret();
    // Convert Base64-encoded secret to HMAC SHA key
    private final Key secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * Generates a short-lived access token for the given user.
     * @param user the user entity to encode into the token
     * @return A signed JWT token
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // Set user email as subject
                .claim("authorities", "ROLE_" + user.getRole().name()) // Store role as "authorities"
                .setIssuedAt(new Date()) // When token was issued
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_15))
                .signWith(secretKey, SignatureAlgorithm.HS256) // Sign with secret key
                .compact();
    }

    /**
     * Generates a longer-lived refresh token for the given user.
     * @param user the user entity to encode
     * @return a signed JWT refresh token
     */
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_2HR))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the email from the token.
     * @param token the JWT token.
     * @return The email encoded.
     */
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

// --Commented out by Inspection START (5/12/2025 7:15 PM):
//    /**
//     * Extracts the user role from the token.
//     * @param token the JWT token.
//     * @return The role encoded in the token.
//     */
//    public String getRoleFromToken(String token) {
//        return (String) Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .get("authorities", String.class);
//    }
// --Commented out by Inspection STOP (5/12/2025 7:15 PM)

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
            return false; // any parse or validation error counts and invalid
        }
    }

    /**
     * Returns the raw secret key used for signing and validation.
     * Useful when other components need to parse or verify JWTs.
     * @return HMAC SHA-256 secret key
     */
    public Key getSecretKey() {
        return secretKey;
    }
}
