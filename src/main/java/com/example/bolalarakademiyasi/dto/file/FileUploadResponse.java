package com.example.bolalarakademiyasi.dto.file;

public record FileUploadResponse(
        String key,
        String url,
        String originalFileName,
        String contentType,
        long size
) {
}
