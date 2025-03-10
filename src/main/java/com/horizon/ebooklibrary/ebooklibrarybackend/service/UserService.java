package com.horizon.ebooklibrary.ebooklibrarybackend.service;

import com.horizon.ebooklibrary.ebooklibrarybackend.model.User;
import com.horizon.ebooklibrary.ebooklibrarybackend.repository.UserRepository;
import com.horizon.ebooklibrary.ebooklibrarybackend.security.JwtUtils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class that handles user authentication and registration
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // Constructor based dependency injection.
    public UserService(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Hashes passwords securely
    }

    /**
     * Registers a new user.
     * Encrypts the password before saving to the database.
     * @param user User object containing email and password.
     */
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password
        userRepository.save(user);
    }

    /**
     * Authenticates a user by checking email and password.
     * If successful, generates a JWT token.
     * @param email User's email.
     * @param password User's password.
     * @return JWT token if authentication is successful, otherwise null/
     */
    public String authenticate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Verify password
            if (passwordEncoder.matches(password, user.getPassword()))
                return jwtUtils.generateToken(user.getEmail()); // Return JWT token
        }

        return null; // Authentication failed
    }
}
