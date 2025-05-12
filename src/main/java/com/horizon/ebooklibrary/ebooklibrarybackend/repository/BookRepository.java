package com.horizon.ebooklibrary.ebooklibrarybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.horizon.ebooklibrary.ebooklibrarybackend.entity.Book;

@SuppressWarnings("unused")
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
