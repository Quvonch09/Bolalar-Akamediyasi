package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.PageBlockResponse;
import com.example.bolalarakademiyasi.dto.request.ReqBook;
import com.example.bolalarakademiyasi.dto.response.ResBook;
import com.example.bolalarakademiyasi.dto.response.ResBookPage;
import com.example.bolalarakademiyasi.entity.enums.PageBlockType;
import com.example.bolalarakademiyasi.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ResBook>>> listBooks() {
        return ResponseEntity.ok(bookService.listBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResBook>> getBook(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @GetMapping("/{id}/pages")
    public ResponseEntity<ApiResponse<List<ResBookPage>>> getBookPages(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getPages(id));
    }

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<String>> createBook(@ModelAttribute ReqBook reqBook) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(reqBook));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<String>> updateBook(@PathVariable UUID id, @ModelAttribute ReqBook reqBook) {
        return ResponseEntity.ok(bookService.updateBook(id, reqBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.deleteBook(id));
    }

    @PostMapping("/{bookId}/pages/{pageId}/blocks")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<String>> createPageBlock(
            @PathVariable UUID bookId,
            @PathVariable UUID pageId,
            @Valid @RequestBody CreatePageBlockRequest request
    ) {
        ApiResponse<String> block = bookService.createPageBlock(
                bookId,
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
                )
        );
        return ResponseEntity.ok(block);
    }

    @PutMapping("/{bookId}/pages/{pageId}/blocks/{blockId}/position")
    public ResponseEntity<ApiResponse<PageBlockResponse>> updatePageBlockPosition(
            @PathVariable UUID bookId,
            @PathVariable UUID pageId,
            @PathVariable UUID blockId,
            @Valid @RequestBody UpdatePageBlockPositionRequest request
    ) {
        ApiResponse<PageBlockResponse> block = bookService.updatePageBlockPosition(
                bookId,
                pageId,
                blockId,
                request.x(),
                request.y()
        );
        return ResponseEntity.ok(block);
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
