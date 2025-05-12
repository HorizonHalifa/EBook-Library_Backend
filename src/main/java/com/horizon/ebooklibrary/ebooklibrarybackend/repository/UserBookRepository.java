package com.horizon.ebooklibrary.ebooklibrarybackend.repository;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Book;
import com.horizon.ebooklibrary.ebooklibrarybackend.entity.User;
import com.horizon.ebooklibrary.ebooklibrarybackend.entity.UserBook;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface UserBookRepository extends JpaRepository<UserBook, Long> {
    Optional<UserBook> findByUserAndBook(User user, Book book);
    List<UserBook> findAllByUser(User user);
    List<UserBook> findAllByUserAndReadTrue(User user);
    List<UserBook> findAllByUserAndReadFalse(User user);
}
