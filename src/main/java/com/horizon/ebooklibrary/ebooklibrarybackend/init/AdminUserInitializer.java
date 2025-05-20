package com.horizon.ebooklibrary.ebooklibrarybackend.init;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Role;
import com.horizon.ebooklibrary.ebooklibrarybackend.entity.User;
import com.horizon.ebooklibrary.ebooklibrarybackend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * AdminUserInitializer ensures a default admin user is created on first run
 * if one doesn't already exist in the database. This replaces the need
 * for init.sql schema bootstrapping.
 */
@Component
@RequiredArgsConstructor
public class AdminUserInitializer {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final String DEFAULT_ADMIN_EMAIL = System.getenv().get("DEFAULT_ADMIN_EMAIL");

    private static final String DEFAULT_ADMIN_PASSWORD = System.getenv().get("DEFAULT_ADMIN_PASSWORD");

    @PostConstruct
    public void initAdminUser() {
        Optional<User> admin = userRepository.findByEmail(DEFAULT_ADMIN_EMAIL);
        if (admin.isEmpty()) {
            User newAdmin = User.builder()
                    .email(DEFAULT_ADMIN_EMAIL)
                    .password(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(newAdmin);
            System.out.println("Default admin user created: " + DEFAULT_ADMIN_EMAIL);
        } else {
            System.out.println("ℹAdmin user already exists: " + DEFAULT_ADMIN_EMAIL);
        }
    }
}
