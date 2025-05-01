package com.horizon.ebooklibrary.ebooklibrarybackend.websocket;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Book;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

/**
 * Costume Spring event that represent the addition of a new book.
 * This event carries the Book object as payload.
 */
@Getter
public class NewBookEvent extends ApplicationEvent {

    private final Book book;

    public NewBookEvent(Object source, Book book) {
        super(source);
        this.book = book;
    }
}
