package com.example.urlshortener.dto;

public record ShortUrlDto(
        Long id,
        String shortCode,
        String originalUrl) {
}

