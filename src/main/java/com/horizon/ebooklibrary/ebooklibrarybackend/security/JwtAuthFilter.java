package com.horizon.ebooklibrary.ebooklibrarybackend.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import org.springframework.lang.NonNull;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Filter that intercepts every incoming HTTP request to:
 * - Extract and validate the JWT token from the Authorization header.
 * - Parse user identity and role from the token.
 * - Authenticate the request in the Spring Security context.
 * <p>
 * If the token is expired or invalid, a 401 Unauthorized response is returned,
 * this ensures that only authenticated users can access protected endpoints.
 */
@SuppressWarnings("unused")
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    /**
     * Filter logic that runs for each request.
     * Extracts and validate JWT, sets authentication context if valid.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Extract Authentication header
        String authHeader = request.getHeader("Authorization");

        // If missing or not a Bearer token, skip filtering
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix

        // Error handling for expired tokens, to return 401 Unauthorized instead of server error.
        try {
            String email = jwtUtils.getEmailFromToken(token);

            // Extract claims from the token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtUtils.getSecretKey()) // Use the secret key from JwtUtils
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Extract user's role from claims
            String role = claims.get("authorities", String.class); // Extract role

            // Convert role into Spring security authorities
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

            // Authenticate if not already authenticated
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                        .username(email)
                        .password("") // Password is not used during token authentication
                        .authorities(authorities)
                        .build();

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        /*
         * Catching ExpiredJwtException and returning 401 unauthorized.
         * Returning a JSON error message "Token has expired. Please login again."
         * Catching other JWT errors, if the token is invalid, return "Invalid token."
         */
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Token has expired. Please log in again.\"}");
            response.getWriter().flush();
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid token.\"}");
            response.getWriter().flush();
            return;
        }


        // Continue request chain
        filterChain.doFilter(request, response);

    }
}
