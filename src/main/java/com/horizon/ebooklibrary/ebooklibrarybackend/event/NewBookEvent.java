package com.horizon.ebooklibrary.ebooklibrarybackend.event;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Book;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

/**
 * Costume Spring application event that signals a bew book has been added.
 * Used internally to decouple book-related logic across the application.
 */
@Getter
public class NewBookEvent extends ApplicationEvent {

    private final Book book;

    /**
     * Construct a new NewBookEvent
     * @param source the object on which the even initially occurred ('this' from publisher)
     * @param book the book that was just added
     */
    public NewBookEvent(Object source, Book book) {
        super(source);
        this.book = book;
    }

}
