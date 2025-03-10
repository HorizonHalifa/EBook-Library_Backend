package com.horizon.ebooklibrary.ebooklibrarybackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user entity in the system.
 * This class is mapped to a "users" table in the database.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor // Generates a default constructor
@Table(name = "users")
public class User {

    /**
     * Unique identifier for each user.
     * Auto incremented by the database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Email of the user.
     * Must be unique and not null/
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Hashed password stored in the database.
     * Must not be null.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Role of the user (default: "USER")
     * Can be used for role based access control.
     */
    @Column(nullable = false)
    private String role = "USER";

    /**
     * Constructor to create a new user with email and password
     * @param email User's email
     * @param password User's hashed password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = "USER"; // Default role
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
