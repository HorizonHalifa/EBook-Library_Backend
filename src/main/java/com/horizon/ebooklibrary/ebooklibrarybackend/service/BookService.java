package com.horizon.ebooklibrary.ebooklibrarybackend.service;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Book;
import com.horizon.ebooklibrary.ebooklibrarybackend.entity.User;
import com.horizon.ebooklibrary.ebooklibrarybackend.entity.UserBook;
import com.horizon.ebooklibrary.ebooklibrarybackend.repository.BookRepository;
import com.horizon.ebooklibrary.ebooklibrarybackend.repository.UserBookRepository;
import com.horizon.ebooklibrary.ebooklibrarybackend.repository.UserRepository;
import com.horizon.ebooklibrary.ebooklibrarybackend.security.JwtUtils;
import com.horizon.ebooklibrary.ebooklibrarybackend.event.BookNotificationPublisher;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Service class for managing books and per-user read/unread status.
 */
@SuppressWarnings("unused")
@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;
    private final UserRepository userRepository;
    private final UploadService uploadService;
    private final JwtUtils jwtUtils;
    private final HttpServletRequest request;
    private final BookNotificationPublisher bookNotificationPublisher;

    /**
     * Get a list of all books in the library (not per-user).
     * @return List of all books in the system
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Retrieve all books that the current authenticated user has marked as read.
     * @return List of read books
     */
    public List<Book> getReadBooks() {
        User user = extractUserFromRequest();
        return userBookRepository.findAllByUserAndReadTrue(user).stream()
                .map(UserBook::getBook)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all books that the current authenticated user has not read.
     * @return List of unread books.
     */
    public List<Book> getUnreadBooks() {
        User user = extractUserFromRequest();
        return userBookRepository.findAllByUserAndReadFalse(user).stream()
                .map(UserBook::getBook)
                .collect(Collectors.toList());
    }

    /**
     * Marks a specific book as read for the current user.
     * @param bookId ID of the book to mark
     */
    public void markAsRead(Long bookId) {
        updateUserBookStatus(bookId, true);
    }

    /**
     * Marks a specific book as unread for the current user.
     * @param bookId ID of the book to mark.
     */
    public void markAsUnread(Long bookId) {
        updateUserBookStatus(bookId, false);
    }


    /**
     * Get a book by it's ID.
     * @param id Book ID
     * @return an optional containing the book, or empty if not found
     */
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * Adds a new book to the library & sends a notification.
     * @param book the book to add
     * @return the saved book
     */
    public Book addBook(Book book) {
        Book savedBook = bookRepository.save(book);

        // Add to user_books as unread for all users
        List<User> users = userRepository.findAll();
        List<UserBook> userBooks = new ArrayList<>();

        for(User user : users) {
            UserBook userBook = UserBook.builder()
                    .user(user)
                    .book(savedBook)
                    .read(false)
                    .build();
            userBooks.add(userBook);
        }

        userBookRepository.saveAll(userBooks);


        // Trigger new book FCM push notification
        bookNotificationPublisher.publishNewBook(savedBook);

        return savedBook;
    }

    /**
     * Deletes a book by ID along with all associated UserBook records and uploaded files.
     * @param id the Id of the book to delete
     */
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));

        try{
            // Delete the cover image file
            uploadService.deleteFile(book.getCoverUrl());

            // Delete the PDF file
            uploadService.deleteFile(book.getPdfUrl());

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete the associated files for book: " + book.getTitle(), e);
        }

        // Delete the book entity (this triggers deletion of associated UserBook entries due to CascadeType.ALL)
        bookRepository.delete(book);
    }

    /**
     * Helper method to update the read/unread status of a book for the authenticated user.
     * @param bookId ID of the book to change
     * @param read true = mark as read; false = mark as unread
     */
    private void updateUserBookStatus(Long bookId, boolean read) {
        User user = extractUserFromRequest();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        UserBook userBook = userBookRepository.findByUserAndBook(user, book)
                .orElse(UserBook.builder()
                        .user(user)
                        .book(book)
                        .build());

        userBook.setRead(read);
        userBookRepository.save(userBook);
    }

    /**
     * Extracts the currently authenticated user from the JWT token in the Authorization header.
     * @return Authenticated User entity
     */
    private User extractUserFromRequest() {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7); // After "Bearer "
        String email = jwtUtils.getEmailFromToken(token);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

}
