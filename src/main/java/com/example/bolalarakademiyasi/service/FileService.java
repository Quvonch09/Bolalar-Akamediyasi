package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.dto.file.FileUploadResponse;
import com.example.bolalarakademiyasi.dto.file.StoredFileResource;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;

public interface FileService {

    FileUploadResponse uploadFile(MultipartFile file);

    FileUploadResponse uploadFile(MultipartFile file, String keyPrefix);

    StoredFileResource downloadFile(String key);

    void deleteFile(String key);

    void deleteFileByUrl(String fileUrl);

    String generatePresignedDownloadUrl(String key);

    String generatePresignedDownloadUrl(String key, Duration expiration);

    String resolveFileUrl(String key);
}
