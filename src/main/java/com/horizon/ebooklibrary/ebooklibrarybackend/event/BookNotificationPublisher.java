package com.horizon.ebooklibrary.ebooklibrarybackend.event;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Book;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Publishes internal Spring events related to book actions.
 * Currently used to publish NewBookEvent when a new book is uploaded.
 */
@SuppressWarnings("unused")
@Component
@RequiredArgsConstructor
public class BookNotificationPublisher {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * Publishes a new NewBookEvent with the provided Book
     * @param book the newly added Book to be broadcast
     */
    public void publishNewBook(Book book) {
        eventPublisher.publishEvent(new NewBookEvent(this, book));
    }
}
