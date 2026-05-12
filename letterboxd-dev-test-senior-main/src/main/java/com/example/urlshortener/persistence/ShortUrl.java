package com.example.urlshortener.persistence;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "short_urls",
        indexes = {
                @Index(name = "idx_original_url", columnList = "originalUrl", unique = true)
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

    @Column(nullable = false)
    private String originalUrl;

    @Column(nullable = false)
    private Instant createdAt;

    public ShortUrl() {
    }

    public ShortUrl(String originalUrl) {
        this.originalUrl = originalUrl;
        this.createdAt = Instant.now();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
