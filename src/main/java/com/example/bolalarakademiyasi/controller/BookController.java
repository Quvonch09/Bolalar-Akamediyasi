package com.example.bolalarakademiyasi.controller;


import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.BookPageResponse;
import com.example.bolalarakademiyasi.dto.BookResponse;
import com.example.bolalarakademiyasi.dto.PageBlockResponse;
import com.example.bolalarakademiyasi.dto.request.ReqBook;
import com.example.bolalarakademiyasi.dto.response.ResBook;
import com.example.bolalarakademiyasi.entity.enums.PageBlockType;
import com.example.bolalarakademiyasi.mapper.BookMapper;
import com.example.bolalarakademiyasi.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;


    @GetMapping
    public ResponseEntity<ApiResponse<List<ResBook>>> listBooks() {
        return ResponseEntity.ok(bookService.listBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBook(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @GetMapping("/{id}/pages")
    public ResponseEntity<ApiResponse<List<BookPageResponse>>> getBookPages(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getPages(id));
    }

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<String>> uploadBook(@RequestBody ReqBook reqBook) {
        return ResponseEntity.ok(bookService.createBook(reqBook));
    }

    @PostMapping("/{bookId}/pages/{pageId}/blocks")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<PageBlockResponse>> createPageBlock(
            @PathVariable UUID bookId,
            @PathVariable UUID pageId,
            @Valid @RequestBody CreatePageBlockRequest request
    ) {

        return ResponseEntity.ok(bookService.createPageBlock(bookId,
                pageId,
                new BookService.PageBlockDraft(
                        request.type(),
                        request.title(),
                        request.body(),
                        request.imageUrl(),
                        request.x(),
                        request.y(),
                        request.width(),
                        request.height()
                )));
    }

    @PutMapping("/{bookId}/pages/{pageId}/blocks/{blockId}/position")
    public ResponseEntity<ApiResponse<PageBlockResponse>> updatePageBlockPosition(
            @PathVariable UUID bookId,
            @PathVariable UUID pageId,
            @PathVariable UUID blockId,
            @Valid @RequestBody UpdatePageBlockPositionRequest request
    ) {
        return ResponseEntity.ok(bookService.updatePageBlockPosition(bookId,
                pageId,
                blockId,
                request.x(),
                request.y()));
    }

    public record CreateBookRequest(
            @NotBlank(message = "Title is required") String title,
            String description,
            MultipartFile file
    ) {
    }

    public record CreatePageBlockRequest(
            @NotNull PageBlockType type,
            @NotBlank String title,
            String body,
            String imageUrl,
            @NotNull Double x,
            @NotNull Double y,
            @NotNull @Positive Double width,
            @NotNull @Positive Double height
    ) {
    }

    public record UpdatePageBlockPositionRequest(
            @NotNull Double x,
            @NotNull Double y
    ) {
    }
}