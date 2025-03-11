package com.horizon.ebooklibrary.ebooklibrarybackend.service;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Role;
import com.horizon.ebooklibrary.ebooklibrarybackend.entity.User;
import com.horizon.ebooklibrary.ebooklibrarybackend.repository.UserRepository;
import com.horizon.ebooklibrary.ebooklibrarybackend.security.JwtUtils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

/**
 * Service class that handles user authentication and registration
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    /**
     * Registers a new user with an encoded password.
     * @param user The user to be registered.
     */
    public void registerUser(User user) {
        // Assign default role if none is provided
        if(user.getRole() == null) {
            user.setRole(Role.USER);
        }
        // Encrypts the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        userRepository.save(user);
    }

    /**
     * Authenticates a user and returns a JWT token
     * @param email User's email.
     * @param password Raw password provided by the user.
     * @return JWT token if authentication is successful, otherwise null
     */
    public String authenticate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Check if the raw password matches the hashed password
            if (passwordEncoder.matches(password, user.getPassword()))
                return jwtUtils.generateToken(user.getEmail(), user.getRole().name()); // Generate JWT token
        }

        return null; // Authentication failed
    }
}
