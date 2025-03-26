package com.horizon.ebooklibrary.ebooklibrarybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByReadTrue();
    List<Book> findByReadFalse();
}
