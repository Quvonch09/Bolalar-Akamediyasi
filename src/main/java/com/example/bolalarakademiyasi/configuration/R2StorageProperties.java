package com.example.bolalarakademiyasi.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "r2")
public record R2StorageProperties(
        @NotBlank String accessKey,
        @NotBlank String secretKey,
        @NotBlank String accountId,
        @NotBlank String endpoint,
        @NotBlank String bucketName,
        String region,
        String publicBaseUrl,
        boolean privateBucket,
        Duration presignedExpiration
) {

    public String regionOrDefault() {
        return (region == null || region.isBlank()) ? "auto" : region;
    }

    public String endpointOrDefault() {
        if (endpoint != null && !endpoint.isBlank() && !endpoint.contains("change-me")) {
            return endpoint;
        }
        return "https://" + accountId + ".r2.cloudflarestorage.com";
    }

    public void validateResolvedConfiguration() {
        if (isPlaceholder(accessKey) || isPlaceholder(secretKey) || isPlaceholder(accountId)) {
            throw new IllegalStateException(
                    "Cloudflare R2 credentials are not configured. Set R2_ACCESS_KEY, R2_SECRET_KEY, and R2_ACCOUNT_ID."
            );
        }

        String resolvedEndpoint = endpointOrDefault();
        if (isPlaceholder(resolvedEndpoint)) {
            throw new IllegalStateException(
                    "Cloudflare R2 endpoint is not configured. Set R2_ENDPOINT or R2_ACCOUNT_ID."
            );
        }
    }

    public Duration presignedExpirationOrDefault() {
        return presignedExpiration == null ? Duration.ofMinutes(15) : presignedExpiration;
    }

    private boolean isPlaceholder(String value) {
        return value == null || value.isBlank() || value.contains("change-me");
    }
}
