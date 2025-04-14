package com.horizon.ebooklibrary.ebooklibrarybackend.controller;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Book;
import com.horizon.ebooklibrary.ebooklibrarybackend.service.BookService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing books in the library.
 * Responsibilities:
 * Provides public endpoints to fetch books.
 * Allows authenticated users to mark books as read/unread.
 * Restricts book creation and deletion to admin users only.
 */
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // Read Methods:

    /**
     * Returns a list of all books in the library.
     * This endpoint is public and does not require authentication.
     * @return List of all books.
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    /**
     * Returns a list of books marked as read.
     * @return list of read books.
     */
    @GetMapping("/read")
    public ResponseEntity<List<Book>> getReadBooks() {
        return ResponseEntity.ok(bookService.getReadBooks());
    }

    /**
     * Returns a list of books marked as unread.
     * @return List of unread books.
     */
    @GetMapping("/unread")
    public ResponseEntity<List<Book>> getUnreadBooks() {
        return ResponseEntity.ok(bookService.getUnreadBooks());
    }

    /**
     * Fetches a single book by it's ID.
     * @param id ID of the book to fetch.
     * @return Book if found, 404 otherwise.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update Methods:

    /**
     * Marks a book as read
     * @param id ID of the book to mark.
     * @return Success message.
     */
    @PutMapping("/{id}/mark-read")
    public ResponseEntity<String> markBookAsRead(@PathVariable Long id) {
        bookService.markAsRead(id);
        return ResponseEntity.ok("Book marked as read.");
    }

    /**
     * Mark book as unread
     * @param id ID of the book to mark.
     * @return Success message.
     */
    @PutMapping("/{id}/mark-unread")
    public ResponseEntity<String> markBookAsUnread(@PathVariable Long id) {
        bookService.markAsUnread(id);
        return ResponseEntity.ok("Book marked as unread.");
    }

    // Admin Methods:

    /**
     * Adds a new book to the library.
     * Admin only access.
     * @param book Book to add.
     * @return Saved book entity.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Only admins can read books
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.addBook(book));
    }

    /**
     * Deletes a book from the library.
     * Admin only access.
     * @param id ID of the book to delete.
     * @return No content if successful.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only admins can delete books
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
