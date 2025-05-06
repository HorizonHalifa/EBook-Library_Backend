package com.horizon.ebooklibrary.ebooklibrarybackend.config;

import com.horizon.ebooklibrary.ebooklibrarybackend.security.JwtAuthFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/*
 * Configuration class for the Spring security.
 *
 * This class:
 * Enables JWT authentication.
 * Defines which endpoints are public and which are protected.
 * Applies @PreAuthorize checks on methods using @EnableMethodSecurity.
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity // Enables @PreAuthorize role-based security
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter; // Injecting JWT filter

    /**
     * Defines security configurations for the application.
     * Public: /auth/** and /books/** for read-only access.
     * Admin: /admin/**
     * Protected: All other endpoints.
     * @param http The HttpSecurity object for configuring security settings.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/ws/**") // Ignore CSRF for WebSockets
                        .disable()) // Disable CSRF for simplicity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // signup / login
                        .requestMatchers(HttpMethod.GET, "/books/**").permitAll() // allow everyone to view books
                        .requestMatchers(HttpMethod.POST, "/books/upload").hasRole("ADMIN") // only admins can upload
                        .requestMatchers("/files/**").permitAll() // allow public access to served PDFs
                        .requestMatchers("/notifications/**").hasRole("ADMIN") // allow admin to send push notifications
                        .requestMatchers("/admin/**").hasRole("ADMIN") // admin only access
                        .anyRequest().authenticated() // Protect all other endpoints
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Apply JWT filter

        return http.build();
    }

    /**
     * Provides password hashing via BCrypt.
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides the AuthenticationManager for authenticating credentials.
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
