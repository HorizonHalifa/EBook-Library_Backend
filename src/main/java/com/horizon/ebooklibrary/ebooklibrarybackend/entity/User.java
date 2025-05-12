package com.horizon.ebooklibrary.ebooklibrarybackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user entity in the system.
 * This class is mapped to a "users" table in the database.
 */
@SuppressWarnings("unused")
@Getter
@Entity
@Setter
@NoArgsConstructor // Generates a default constructor
@AllArgsConstructor
@Builder
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
     * Stored as String in the database
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserBook> userBooks;

    /**
     * Constructor to create a new user with email and password
     * @param email User's email
     * @param password User's hashed password
     * @param role User role (USER or ADMIN), defaults to USER if null
     */
    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = (role != null) ? role : Role.USER; // Defaults role if null
    }



}
