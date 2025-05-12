package com.horizon.ebooklibrary.ebooklibrarybackend.security;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class made in order to simplifier and separate the big logic in JwtUtils.
 * This class will be responsible to load the key used for the backend JWT token creation encryption.
 */
@SuppressWarnings("unused")
public class JwtSecretLoader {

    public static String loadJwtSecret() {
        try {
            String keyPath = System.getenv("JWT_SECRET_PATH");
            if(keyPath == null || keyPath.isEmpty()) {
                throw new IllegalStateException("JWT_SECRET_PATH environment variable not set.");
            }
            return Files.readString(Paths.get(keyPath)).trim();
        } catch(IOException e) {
            throw new RuntimeException("Failed to read JWT secret key from file: " + e.getMessage(), e);
        }
    }
}
