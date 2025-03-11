package com.horizon.ebooklibrary.ebooklibrarybackend.config;

import com.horizon.ebooklibrary.ebooklibrarybackend.security.JwtAuthFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
 * Disables default authentication mechanisms (removed the auto-generated password).
 * Configures public and protected API endpoints.
 * Ensures JWT authentication is applied correctly.
 * Registers PasswordEncoder to securely hash the password
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity // Enables @PreAuthorize role-based security
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter; // Injecting JWT filter

    /**
     * Defines security configurations for the application
     * @param http The HttpSecurity object for configuring security settings.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // public access for authentication routes
                        .requestMatchers("/h2-console/**").permitAll() // allow H2 console
                        .requestMatchers("/api/**").authenticated() // require authentication for general API
                        .requestMatchers("/admin/**").hasRole("ADMIN") // admin only access
                        .anyRequest().authenticated() // Protect all other endpoints
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // allow H2 Console frames
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Apply JWT filter

        return http.build();
    }

    /**
     * Registers a PasswordEncoder bean to securely hash user passwords.
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Hashes password securely
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
