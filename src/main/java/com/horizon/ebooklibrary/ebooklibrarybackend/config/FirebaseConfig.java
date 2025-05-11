package com.horizon.ebooklibrary.ebooklibrarybackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

/**
 * Configuration class to initialize Firebase Admin SDK using service account credentials.
 */
@Configuration
public class FirebaseConfig {

    /**
     * Initializes FirebaseApp on application startup using the service account credentials.
     */
    @PostConstruct
    public void initialize() {
        try {
            // Assume file is in src/main/resources/
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("ebooklibrarybackend-firebase-adminsdk-fbsvc-3baa5ed9ea.json");

            if(serviceAccount == null) {
                throw new IllegalStateException("Firebase service account file not found in resources.");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase has been initialized.");
        } catch( IOException e) {
            throw new RuntimeException("Failed to initialize Firebase.");
        }
    }
}
