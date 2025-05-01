package com.horizon.ebooklibrary.ebooklibrarybackend.websocket;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Book;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * This class is responsible for publishing a Spring event when a new book is added.
 * It decouples the service logic (BookService) from WebSocket/Web events.
 */
@Component
@RequiredArgsConstructor
public class BookNotificationPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishNewBook(Book book) {
        eventPublisher.publishEvent(new NewBookEvent(this, book));
    }
}
