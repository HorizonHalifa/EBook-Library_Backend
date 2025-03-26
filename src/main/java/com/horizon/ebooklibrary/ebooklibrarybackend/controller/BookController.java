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

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * Retrieve all books
     * @return Response Entity List of all books.
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    /**
     * Get all read books
     * @return response entity containing read books
     */
    @GetMapping("/read")
    public ResponseEntity<List<Book>> getReadBooks() {
        return ResponseEntity.ok(bookService.getReadBooks());
    }

    /**
     * Get all unread books
     * @return response entity containing unread books
     */
    @GetMapping("/unread")
    public ResponseEntity<List<Book>> getUnreadBooks() {
        return ResponseEntity.ok(bookService.getUnreadBooks());
    }

    /**
     * Marks a book as read
     * @param id the ID of the book to mark as read
     * @return "Book marked as read" if succeeded
     */
    @PutMapping("/{id}/mark-read")
    public ResponseEntity<String> markBookAsRead(@PathVariable Long id) {
        bookService.markAsRead(id);
        return ResponseEntity.ok("Book marked as read.");
    }

    /**
     * Mark book as unread
     * @param id book to mark as unread
     * @return "Book marked as unread" if succeeded
     */
    @PutMapping("/{id}/mark-unread")
    public ResponseEntity<String> markBookAsUnread(@PathVariable Long id) {
        bookService.markAsUnread(id);
        return ResponseEntity.ok("Book marked as unread.");
    }

    /**
     * Retrieves book by id
     * @param id to search by
     * @return Book that matches id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Only admins can read books
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.addBook(book));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only admins can delete books
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}


