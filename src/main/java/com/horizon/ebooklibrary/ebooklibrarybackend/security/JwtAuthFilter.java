package com.horizon.ebooklibrary.ebooklibrarybackend.security;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Role;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Filter to handle JWT authentication in each request
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // Error handling for expired tokens, to return 401 Unauthorized instead of server error.
        try {
            String email = jwtUtils.getEmailFromToken(token);

            // Extract claims from the token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtUtils.getSecretKey()) // Use the secret key from JwtUtils
                    .build()
                    .parseClaimsJws(token)
                    .getBody();


            String role = claims.get("authorities", String.class); // Extract role

            // Convert role into Spring security authorities
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                        .username(email)
                        .password("")
                        .authorities(authorities)
                        .build();

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
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

        filterChain.doFilter(request, response);

    }
}
