package com.example.bolalarakademiyasi.mapper;

import com.example.bolalarakademiyasi.dto.BookPageResponse;
import com.example.bolalarakademiyasi.dto.BookResponse;
import com.example.bolalarakademiyasi.dto.PageBlockResponse;
import com.example.bolalarakademiyasi.dto.response.ResBook;
import com.example.bolalarakademiyasi.dto.response.ResBookPage;
import com.example.bolalarakademiyasi.entity.Book;
import com.example.bolalarakademiyasi.entity.BookPage;
import com.example.bolalarakademiyasi.entity.PageBlock;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {

    public ResBook resBook(Book book){
        return ResBook.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .status(book.getStatus().name())
                .subjectName(book.getSubject() != null ? book.getSubject().getName() : null)
                .subjectId(book.getSubject() != null ? book.getSubject().getId() : null)
                .coverImageUrl(resolveUrl(book.getCoverImageUrl()))
                .fileUrl(resolveUrl(book.getFileUrl()))
                .originalFileName(book.getOriginalFileName())
                .gradeLevel(book.getGradeLevel())
                .lessonId(book.getLesson() != null ? book.getLesson().getId() : null)
                .lessonName(book.getLesson() != null ? book.getLesson().getName() : null)
                .totalPages(book.getTotalPages())
                .pages(book.getPages().stream().map(this::toPageResponse).toList())
                .build();
    }


    public ResBook resBookDto(Book book){
        return ResBook.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .status(book.getStatus().name())
                .subjectName(book.getSubject() != null ? book.getSubject().getName() : null)
                .fileUrl(resolveUrl(book.getFileUrl()))
                .coverImageUrl(resolveUrl(book.getCoverImageUrl()))
                .gradeLevel(book.getGradeLevel())
                .subjectId(book.getSubject() != null ? book.getSubject().getId() : null)
                .lessonName(book.getLesson() != null ? book.getLesson().getName() : null)
                .lessonId(book.getLesson() != null ? book.getLesson().getId() : null)
                .totalPages(book.getTotalPages())
                .build();
    }


    public ResBookPage resBookPage(BookPage bookPage){
        return ResBookPage.builder()
                .id(bookPage.getId())
                .bookId(bookPage.getBook() != null ? bookPage.getBook().getId() : null)
                .bookTitle(bookPage.getBook() != null ? bookPage.getBook().getTitle() : null)
                .width(bookPage.getWidth())
                .height(bookPage.getHeight())
                .blocks(bookPage.getBlocks().stream().map(this::toBlockResponse).toList())
                .pageNumber(bookPage.getPageNumber())
                .imageUrl(resolveUrl(bookPage.getImageUrl()))
                .build();
    }


    public BookResponse toResponse(Book book, List<BookPage> pages) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getOriginalFileName(),
                resolveUrl(book.getFileUrl()),
                resolveUrl(book.getCoverImageUrl()),
                book.getStatus().name(),
                book.getTotalPages(),
                book.getCreatedAt(),
                pages.stream().map(this::toPageResponse).toList()
        );
    }

    public BookPageResponse toPageResponse(BookPage page) {
        return new BookPageResponse(
                page.getId(),
                page.getPageNumber(),
                resolveUrl(page.getImageUrl()),
                page.getWidth(),
                page.getHeight(),
                page.getBlocks().stream().map(this::toBlockResponse).toList()
        );
    }

    public PageBlockResponse toBlockResponse(PageBlock block) {
        return new PageBlockResponse(
                block.getId(),
                block.getType().name(),
                block.getTitle(),
                block.getBody(),
                block.getImageUrl(),
                block.getX(),
                block.getY(),
                block.getWidth(),
                block.getHeight(),
                block.getCreatedAt()
        );
    }

    private String resolveUrl(String... candidates) {
        for (String candidate : candidates) {
            if (candidate != null && !candidate.isBlank()) {
                return candidate;
            }
        }
        return null;
    }
}
