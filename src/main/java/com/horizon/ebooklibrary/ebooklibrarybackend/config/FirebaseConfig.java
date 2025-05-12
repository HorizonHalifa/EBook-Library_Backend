package com.horizon.ebooklibrary.ebooklibrarybackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

/**
 * Configuration class to initialize Firebase Admin SDK using service account credentials.
 */
@SuppressWarnings("unused")
@Configuration
public class FirebaseConfig {

    /**
     * Initializes FirebaseApp on application startup using the service account credentials.
     */
    @PostConstruct
    public void initialize() {
        try {

            String path = System.getenv("FIREBASE_CREDENTIALS_PATH");
            if(path == null || path.isBlank()) {
                throw new IllegalStateException("FIREBASE_CREDENTIALS_PATH env variable is not set.");
            }

            FileInputStream serviceAccount = new FileInputStream(path);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("Firebase has been initialized.");

        } catch( IOException e) {
            throw new RuntimeException("Failed to initialize Firebase.");
        }
    }
}
