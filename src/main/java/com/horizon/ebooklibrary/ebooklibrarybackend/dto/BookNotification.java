package com.horizon.ebooklibrary.ebooklibrarybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing book notification content for WebSocket
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookNotification {
    private String title;
    private String author;
    private String message;
}
