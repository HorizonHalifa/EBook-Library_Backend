package com.horizon.ebooklibrary.ebooklibrarybackend.repository;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    static Optional<User> findByEmail(String email) // Find user by email
    {
        return Optional.empty();
    }
}
