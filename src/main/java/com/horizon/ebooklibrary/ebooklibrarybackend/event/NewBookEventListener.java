package com.horizon.ebooklibrary.ebooklibrarybackend.event;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Book;
import com.horizon.ebooklibrary.ebooklibrarybackend.service.FCMService;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Listens for NewBookEvent and sends a broadcast push notification to all users.
 * Subscribed to the "new_books" topic via Firebase Cloud Messaging.
 */
@Component
@RequiredArgsConstructor
public class NewBookEventListener {

    private final FCMService fcmService;

    @EventListener
    public void handleNewBookEvent(NewBookEvent event) {
        Book book = event.getBook();
        try {
            fcmService.sendToTopic(
                    "new_books",
                    "New Book: " + book.getTitle(),
                    "By " + book.getAuthor() + " - now in the library!"
            );
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
