package com.horizon.ebooklibrary.ebooklibrarybackend.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing the relationship between a User and a Book,
 * including the user's reading status for that specific book.
 * This allow sus to track which books each user has read or not.
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "user_books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBook {

    /**
     * Unique ID for this user-book relationship
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The User who owns this book relationship.
     * Many UseBook entries can point to the same User.
     */
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    /**
     * The Book that is being tracked for the user.
     * Many UserBook entries can point to the same Book.
     */
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Book book;

    /**
     * Whether this User has marked the book as read.
     */
    @Column(nullable = false)
    private boolean read;
}
