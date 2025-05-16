package com.horizon.ebooklibrary.ebooklibrarybackend.service;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Book;
import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Role;
import com.horizon.ebooklibrary.ebooklibrarybackend.entity.User;
import com.horizon.ebooklibrary.ebooklibrarybackend.entity.UserBook;
import com.horizon.ebooklibrary.ebooklibrarybackend.repository.BookRepository;
import com.horizon.ebooklibrary.ebooklibrarybackend.repository.UserBookRepository;
import com.horizon.ebooklibrary.ebooklibrarybackend.repository.UserRepository;
import com.horizon.ebooklibrary.ebooklibrarybackend.security.JwtUtils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

/**
 * Service class that handles user authentication and registration.
 * Initializes new users with all existing books marked as unread.
 */
@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    /**
     * Registers a new user with an encoded password.
     * By default assigns to the user the 'USER' role and all books as unread.
     * @param user The user to be registered.
     */
    public void registerUser(User user) {
        // Assign default role if none is provided
        if(user.getRole() == null) {
            user.setRole(Role.USER);
        }
        // Encrypts the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user to database
        User savedUser = userRepository.save(user);

        // Fetch all existing books
        List<Book> allBooks = bookRepository.findAll();

        // Create UserBook entries for all books as unread
        List<UserBook> userBooks = allBooks.stream()
                .map(book -> UserBook.builder()
                        .user(savedUser)
                        .book(book)
                        .read(false)
                        .build())
                .collect(Collectors.toList());

        // Save all UserBook entries in bulk
        userBookRepository.saveAll(userBooks);
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
                return jwtUtils.generateToken(user); // Generate JWT token
        }

        return null; // Authentication failed
    }

    /**
     * find a user by their email.
     * @param email the email of the user.
     * @return The User object if found, otherwise null.
     */
    // Encapsulation: UserService should be the only place that interacts with UserRepository.
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
