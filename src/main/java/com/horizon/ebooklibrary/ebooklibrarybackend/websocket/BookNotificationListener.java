package com.horizon.ebooklibrary.ebooklibrarybackend.websocket;

import com.horizon.ebooklibrary.ebooklibrarybackend.dto.BookNotification;
import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Listens for NewBookEvent and sends WebSocket notification to users.
 */
@Component
@RequiredArgsConstructor
public class BookNotificationListener {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Handles a NewBookEvent by sending a WebSocket notification
     * @param event The event triggered when a new book is added
     */
    @EventListener
    public void handleNewBookEvent(NewBookEvent event) {
        Book book = event.getBook();

        BookNotification notification = new BookNotification(
                book.getTitle(),
                book.getAuthor(),
                "A new book has been added!"
        );

        // Send to the topic "topic/new-books" so users will receive this notification
        messagingTemplate.convertAndSend("/topic/new-books", notification);
    }

}
