package com.horizon.ebooklibrary.ebooklibrarybackend.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for sending push notifications using Firebase Cloud Messaging (FCM).
 * Encapsulates FCM logic to isolate messaging from business logic.
 */
@SuppressWarnings("unused")
@Slf4j
@Service
public class FCMService {

    public static final String TOPIC_NEW_BOOKS = "new_books";

    /**
     * Send a push notification to a given topic
     * @param topic the FCM topic (without /topics/ prefix)
     * @param title the notification title
     * @param body the notification body
     * @throws FirebaseMessagingException if the send fails
     */
    public void sendToTopic(String topic, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setTopic(topic)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        log.info("FCM message sent to topic '{}': {}", topic, response);
    }

}
