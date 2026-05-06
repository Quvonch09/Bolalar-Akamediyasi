package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.file.FileUploadResponse;
import com.example.bolalarakademiyasi.dto.file.StoredFileResource;
import com.example.bolalarakademiyasi.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(name = "Files", description = "Cloudflare R2 file storage APIs")
public class FileController {
    private final FileService fileService;

    @PostMapping(value = "/upload",consumes = "multipart/form-data")
    @Operation(summary = "Upload a file to Cloudflare R2")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile(@RequestParam("file") MultipartFile file) {
        FileUploadResponse response = fileService.uploadFile(file);
        return ResponseEntity.ok(ApiResponse.success(response, "File uploaded successfully"));
    }

    @GetMapping("/{*key}")
    @Operation(summary = "Download a file from Cloudflare R2")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String key) {
        StoredFileResource file = fileService.downloadFile(key);

        // Force download so browsers do not try to render binary files inline by default.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(file.contentType()));
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(file.originalFileName())
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .body(file.resource());
    }

    @DeleteMapping("/{*key}")
    @Operation(summary = "Delete a file from Cloudflare R2")
    public ResponseEntity<ApiResponse<Void>> deleteFile(@PathVariable String key) {
        fileService.deleteFile(key);
        return ResponseEntity.ok(ApiResponse.success(null, "File deleted successfully"));
    }
}
