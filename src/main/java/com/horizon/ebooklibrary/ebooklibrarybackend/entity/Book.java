package com.horizon.ebooklibrary.ebooklibrarybackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto increment ID
    private long ID;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(columnDefinition = "text") // Long text for description
    private String description;

    @Column(nullable = false)
    private String coverUrl; // URL for the book cover image

    @Column(nullable = false)
    private String pdfUrl; // URL to access a book file

    @Column(nullable = false)
    private boolean read = false; // Default: Unread
}
