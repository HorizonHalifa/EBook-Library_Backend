package com.horizon.ebooklibrary.ebooklibrarybackend.service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import org.springframework.stereotype.Service;

/**
 * Service responsible for sending push notifications using Firebase Cloud Messaging (FCM).
 * This service builds and sends a message to a given device token.
 */
@Service
public class FCMService {

    /**
     * Sends a push notification to a specific device using it's FCM token.
     * @param targetToken FCM registration token of the target device
     * @param title Title of the notification
     * @param body Body text of the notification
     * @throws Exception if message sending fails
     */
    public void sendPushNotification(String targetToken, String title, String body) throws Exception {
        // targetToken is the unique token each device receives from Firebase.
        Message message = Message.builder()
                .setToken(targetToken)

                // Notification is the actual visual notification the user sees
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())

                // AndroidConfig allows setting platform-specific behaviours (like notification sounds or click actions)
                .setAndroidConfig(AndroidConfig.builder()
                        .setNotification(AndroidNotification.builder()
                                .setSound("default")
                                .setClickAction("OPEN_BOOK_LIST")
                                .build())
                        .build())
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Successfully sent message: " + response);
    }
}
