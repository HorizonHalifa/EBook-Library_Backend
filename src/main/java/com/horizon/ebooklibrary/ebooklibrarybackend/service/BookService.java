package com.horizon.ebooklibrary.ebooklibrarybackend.service;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Book;
import com.horizon.ebooklibrary.ebooklibrarybackend.repository.BookRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private BookRepository bookRepository;

    /**
     * Fetch all books (both read and unread)
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Fetch all read books
     * @return List of read books
     */
    public List<Book> getReadBooks() {
        return bookRepository.findByReadTrue();
    }

    /**
     * Fetch all unread books
     * @return List of unread books
     */
    public List<Book> getUnreadBooks() {
        return bookRepository.findByReadFalse();
    }

    /**
     * Fetch book by id
     * @param id the id to search
     * @return book that has the requested id
     */
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * Add a new book to the database
     * @param book to add
     * @return the saved book
     */
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Mark book as read
     * @param id book to mark as read
     */
    public void markAsRead(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if(optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setRead(true);
            bookRepository.save(book);
        }
    }

    /**
     * Mark book as unread
     * @param id book to mark unread
     */
    public void markAsUnread(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if(optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setRead(false);
            bookRepository.save(book);
        }
    }

    /**
     * Delete book by id
     * @param id to delete by
     */
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
