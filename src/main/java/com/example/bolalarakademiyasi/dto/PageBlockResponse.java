package com.example.bolalarakademiyasi.dto;

import java.time.Instant;
import java.util.UUID;

public record PageBlockResponse(
        UUID id,
        String type,
        String title,
        String body,
        String imageUrl,
        double x,
        double y,
        double width,
        double height,
        Instant createdAt
) {
}
