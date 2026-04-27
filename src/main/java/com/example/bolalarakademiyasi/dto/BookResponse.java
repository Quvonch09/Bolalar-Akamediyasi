package com.example.bolalarakademiyasi.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record BookResponse(
        UUID id,
        String title,
        String description,
        String originalFileName,
        String pdfUrl,
        String coverImageUrl,
        String status,
        int totalPages,
        Instant createdAt,
        List<BookPageResponse> pages
) {
}
