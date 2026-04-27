package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.Book;
import com.example.bolalarakademiyasi.entity.BookPage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookPageRepository extends JpaRepository<BookPage, UUID> {
    @EntityGraph(attributePaths = "blocks")
    List<BookPage> findByBookIdOrderByPageNumberAsc(UUID bookId);

    @EntityGraph(attributePaths = "blocks")
    java.util.Optional<BookPage> findByIdAndBookId(UUID pageId, UUID bookId);
}
