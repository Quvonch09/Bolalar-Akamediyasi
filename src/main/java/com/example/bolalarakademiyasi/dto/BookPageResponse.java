package com.example.bolalarakademiyasi.dto;

import java.util.UUID;

public record BookPageResponse(
        UUID id,
        int pageNumber,
        String imageUrl,
        int width,
        int height,
        java.util.List<PageBlockResponse> blocks
) {
}
