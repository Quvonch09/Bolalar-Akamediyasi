package com.example.bolalarakademiyasi.dto.file;

import org.springframework.core.io.InputStreamResource;

public record StoredFileResource(
        InputStreamResource resource,
        String key,
        String originalFileName,
        String contentType,
        long contentLength
) {
}
