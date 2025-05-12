package com.horizon.ebooklibrary.ebooklibrarybackend.event;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Book;
import com.horizon.ebooklibrary.ebooklibrarybackend.service.FCMService;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Listens for NewBookEvent and sends a broadcast push notification to all users.
 * Subscribed to the "new_books" topic via Firebase Cloud Messaging.
 */
@SuppressWarnings("unused")
@Slf4j
@Component
@RequiredArgsConstructor
public class NewBookEventListener {

    private final FCMService fcmService;

    @EventListener
    public void handleNewBookEvent(NewBookEvent event) {
        Book book = event.getBook();
        try {
            fcmService.sendToTopic(
                    FCMService.TOPIC_NEW_BOOKS,
                    "New Book: " + book.getTitle(),
                    "By " + book.getAuthor() + " - now in the library!"
            );
            log.info("Notification sent for book '{}'", book.getTitle());
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send FCM notification for new book '{}': {}", book.getTitle(), e.getMessage(), e);
        }
    }
}
