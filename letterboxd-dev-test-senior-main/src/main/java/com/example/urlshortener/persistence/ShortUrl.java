package com.example.urlshortener.persistence;

import jakarta.persistence.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;

@Entity
@Table(
        name = "short_urls",
        indexes = {
                @Index(name = "idx_original_url", columnList = "originalUrl", unique = true),
                @Index(name = "idx_url_hash", columnList = "urlHash", unique = true)
        }
)
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "short_url_seq")
    @SequenceGenerator(
            name = "short_url_seq",
            sequenceName = "short_url_seq",
            initialValue = 100000,
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, length = 2048, unique = true)
    private String originalUrl;

    @Column(nullable = false, length = 64, unique = true)
    private String urlHash;

    @Column(nullable = false)
    private Instant createdAt;

    public ShortUrl() {
    }

    public ShortUrl(String originalUrl) {
        this.originalUrl = originalUrl;
        this.urlHash = getHash(originalUrl);
        this.createdAt = Instant.now();
    }

    public static String getHash(String url) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(url.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash URL", e);
        }
    }

    public Long getId() {
        return id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getUrlHash() {
        return urlHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}