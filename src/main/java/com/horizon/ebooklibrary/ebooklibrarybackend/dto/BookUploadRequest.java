package com.horizon.ebooklibrary.ebooklibrarybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for uploading a new book with metadate (excluding file).
 * Used in multipcart/form.data requests when admins upload a book + it's PDF together.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookUploadRequest {

    private String title;
    private String author;
    private String description;
    private String coverUrl;
}
