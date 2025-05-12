package com.horizon.ebooklibrary.ebooklibrarybackend.repository;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@SuppressWarnings("unused")
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Find user by email
}
