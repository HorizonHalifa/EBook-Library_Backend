package com.horizon.ebooklibrary.ebooklibrarybackend.init;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Role;
import com.horizon.ebooklibrary.ebooklibrarybackend.entity.User;
import com.horizon.ebooklibrary.ebooklibrarybackend.repository.UserRepository;
import com.horizon.ebooklibrary.ebooklibrarybackend.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * AdminUserInitializer ensures a default admin user is created on first run if one doesn't already exist in the database.
 */
@Component
@RequiredArgsConstructor
public class AdminUserInitializer {

    private final UserRepository userRepository;
    private final UserService userService;

    private static final String DEFAULT_ADMIN_EMAIL = System.getenv().get("DEFAULT_ADMIN_EMAIL");

    private static final String DEFAULT_ADMIN_PASSWORD = System.getenv().get("DEFAULT_ADMIN_PASSWORD");

    @PostConstruct
    public void initAdminUser() {
        if(DEFAULT_ADMIN_EMAIL == null || DEFAULT_ADMIN_PASSWORD == null) {
            System.err.println("Admin credentials not set in environment. Skipping admin user initialization.");
        }

        Optional<User> existingAdmin = userRepository.findByEmail(DEFAULT_ADMIN_EMAIL);
        if (existingAdmin.isEmpty()) {
            User newAdmin = User.builder()
                    .email(DEFAULT_ADMIN_EMAIL)
                    .password(DEFAULT_ADMIN_PASSWORD)
                    .role(Role.ADMIN) // Force ADMIN instead of default USER
                    .build();

            userService.registerUser(newAdmin);
            System.out.println("Default admin user created: " + DEFAULT_ADMIN_EMAIL);
        } else {
            System.out.println("Admin user already exists: " + DEFAULT_ADMIN_EMAIL);
        }
    }
}
