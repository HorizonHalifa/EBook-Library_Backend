package com.horizon.ebooklibrary.ebooklibrarybackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/*
 * Utility class for handling JWT operations such as token generation and validation.
 */
@Component
public class JwtUtils {
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    /* HMAC with SHA-256 is probably the best choice for small scale project
     * where I have control of both the client and the server, for simplicity and performance.
     */
    private static final String SECRET = "+pu/Q8KgBbnGUJ/MKA/meHBAAekvMt+Y+CzD+GHI/fw=";
    private final Key secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET));

    /**
     * Generates a JWT token for the given user email.
     * @param email the email to include in the token.
     * @return A signed JWT token.
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // Set user email as subject
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
