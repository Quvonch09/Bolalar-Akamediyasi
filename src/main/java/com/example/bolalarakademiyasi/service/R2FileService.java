package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.configuration.R2StorageProperties;
import com.example.bolalarakademiyasi.dto.file.FileUploadResponse;
import com.example.bolalarakademiyasi.dto.file.StoredFileResource;
import com.example.bolalarakademiyasi.exception.BadRequestException;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class R2FileService implements FileService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final R2StorageProperties properties;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file) {
        return uploadFile(file, null);
    }

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, String keyPrefix) {
        validateFile(file);

        // Keep the original extension, but never trust the client filename as the object key.
        String key = buildObjectKey(file.getOriginalFilename(), keyPrefix);
        String contentType = resolveContentType(file.getContentType());

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(properties.bucketName())
                .key(key)
                .contentType(contentType)
                .contentLength(file.getSize())
                .metadata(java.util.Map.of(
                        "original-filename", safeOriginalFileName(file.getOriginalFilename())
                ))
                .build();

        try {
            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
            return new FileUploadResponse(
                    key,
                    resolveFileUrl(key),
                    safeOriginalFileName(file.getOriginalFilename()),
                    contentType,
                    file.getSize()
            );
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read uploaded file bytes", ex);
        } catch (SdkException ex) {
            throw new IllegalStateException(
                    "Failed to upload file to Cloudflare R2. Endpoint: " + properties.endpointOrDefault(),
                    ex
            );
        }
    }

    @Override
    public StoredFileResource downloadFile(String key) {
        String normalizedKey = normalizeKey(key);

        try {
            HeadObjectResponse metadata = s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(properties.bucketName())
                    .key(normalizedKey)
                    .build());

            ResponseInputStream<GetObjectResponse> stream = s3Client.getObject(GetObjectRequest.builder()
                    .bucket(properties.bucketName())
                    .key(normalizedKey)
                    .build());

            String originalFileName = metadata.metadata().getOrDefault(
                    "original-filename",
                    normalizedKey
            );

            // Stream directly from R2 instead of loading large files fully into memory.
            return new StoredFileResource(
                    new InputStreamResource(stream),
                    normalizedKey,
                    originalFileName,
                    resolveContentType(metadata.contentType()),
                    metadata.contentLength()
            );
        } catch (NoSuchKeyException ex) {
            throw new DataNotFoundException("File not found: " + normalizedKey);
        } catch (S3Exception ex) {
            if (ex.statusCode() == 404) {
                throw new DataNotFoundException("File not found: " + normalizedKey);
            }
            throw new IllegalStateException("Failed to download file from Cloudflare R2", ex);
        }
    }

    @Override
    public void deleteFile(String key) {
        String normalizedKey = normalizeKey(key);
        assertFileExists(normalizedKey);

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(properties.bucketName())
                    .key(normalizedKey)
                    .build());
        } catch (SdkException ex) {
            throw new IllegalStateException("Failed to delete file from Cloudflare R2", ex);
        }
    }

    @Override
    public void deleteFileByUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }
        deleteFile(extractKeyFromUrl(fileUrl));
    }

    @Override
    public String generatePresignedDownloadUrl(String key) {
        return generatePresignedDownloadUrl(key, properties.presignedExpirationOrDefault());
    }

    @Override
    public String generatePresignedDownloadUrl(String key, Duration expiration) {
        String normalizedKey = normalizeKey(key);
        assertFileExists(normalizedKey);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(properties.bucketName())
                .key(normalizedKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Objects.requireNonNullElse(expiration, properties.presignedExpirationOrDefault()))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    @Override
    public String resolveFileUrl(String key) {
        String normalizedKey = normalizeKey(key);

        if (properties.privateBucket()) {
            return null;
        }

        String publicBaseUrl = properties.publicBaseUrl();
        if (publicBaseUrl != null && !publicBaseUrl.isBlank()) {
            return trimTrailingSlash(publicBaseUrl) + "/" + urlEncodePath(normalizedKey);
        }

        return trimTrailingSlash(properties.endpointOrDefault())
                + "/" + properties.bucketName()
                + "/" + urlEncodePath(normalizedKey);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File must not be empty");
        }
    }

    private void assertFileExists(String key) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(properties.bucketName())
                    .key(key)
                    .build());
        } catch (S3Exception ex) {
            if (ex.statusCode() == 404) {
                throw new DataNotFoundException("File not found: " + key);
            }
            throw new IllegalStateException("Failed to check file in Cloudflare R2", ex);
        }
    }

    private String buildObjectKey(String originalFileName, String keyPrefix) {
        String extension = extractExtension(originalFileName);
        String generatedName = UUID.randomUUID() + extension;

        if (keyPrefix == null || keyPrefix.isBlank()) {
            return generatedName;
        }

        String cleanedPrefix = keyPrefix.trim().replace("\\", "/");
        while (cleanedPrefix.startsWith("/")) {
            cleanedPrefix = cleanedPrefix.substring(1);
        }
        while (cleanedPrefix.endsWith("/")) {
            cleanedPrefix = cleanedPrefix.substring(0, cleanedPrefix.length() - 1);
        }

        return cleanedPrefix.isBlank() ? generatedName : cleanedPrefix + "/" + generatedName;
    }

    private String extractExtension(String originalFileName) {
        if (originalFileName == null || originalFileName.isBlank()) {
            return "";
        }

        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == originalFileName.length() - 1) {
            return "";
        }

        return originalFileName.substring(dotIndex).toLowerCase(Locale.ROOT);
    }

    private String safeOriginalFileName(String originalFileName) {
        if (originalFileName == null || originalFileName.isBlank()) {
            return "file";
        }
        return originalFileName;
    }

    private String resolveContentType(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return contentType;
    }

    private String normalizeKey(String key) {
        if (key == null || key.isBlank()) {
            throw new BadRequestException("File key is required");
        }
        return key.trim();
    }

    private String trimTrailingSlash(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private String urlEncodePath(String path) {
        return java.util.Arrays.stream(path.split("/"))
                .map(segment -> URLEncoder.encode(segment, StandardCharsets.UTF_8).replace("+", "%20"))
                .reduce((left, right) -> left + "/" + right)
                .orElse("");
    }

    private String extractKeyFromUrl(String fileUrl) {
        URI uri = URI.create(fileUrl.trim());
        String path = uri.getPath();
        String publicBaseUrl = properties.publicBaseUrl();

        if (publicBaseUrl != null && !publicBaseUrl.isBlank()) {
            return decodePath(trimLeadingSlash(path));
        }

        String bucketPrefix = "/" + properties.bucketName() + "/";
        int bucketIndex = path.indexOf(bucketPrefix);
        if (bucketIndex >= 0) {
            return decodePath(path.substring(bucketIndex + bucketPrefix.length()));
        }

        return decodePath(trimLeadingSlash(path));
    }

    private String trimLeadingSlash(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.startsWith("/") ? value.substring(1) : value;
    }

    private String decodePath(String path) {
        return URLDecoder.decode(path, StandardCharsets.UTF_8);
    }
}
