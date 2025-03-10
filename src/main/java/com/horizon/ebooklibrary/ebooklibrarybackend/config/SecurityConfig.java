package com.horizon.ebooklibrary.ebooklibrarybackend.config;

import com.horizon.ebooklibrary.ebooklibrarybackend.security.JwtUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Disables default Spring Boot authentication (removed the auto-generated password).
 * Allows /auth/signup and /auth/login to be publicly accessible.
 * Requires authentication for all other APIs (e.g. fetching books).
 * Ensures passwords are securely hashed with BCryptPasswordEncoder.
 */
@Configuration
public class SecurityConfig {

    private final JwtUtils jwtUtils;

    public SecurityConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/signup", "/auth/login").permitAll() // Allow public access to auth APIs
                        .requestMatchers("/api/hello").authenticated() // Protected endpoint
                        .anyRequest().authenticated() // Protects all other endpoints
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // No sessions

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
