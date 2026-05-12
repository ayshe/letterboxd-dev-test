package com.example.urlshortener.services.shorturlservice.impl;

import com.example.urlshortener.dto.ShortUrlDto;
import com.example.urlshortener.exception.CodeNotFoundException;
import com.example.urlshortener.exception.FormatException;
import com.example.urlshortener.persistence.ShortUrl;
import com.example.urlshortener.persistence.ShortUrlRepository;
import com.example.urlshortener.services.encoding.Crockford32Encoder;
import com.example.urlshortener.services.shorturlservice.ShortUrlService;
import com.example.urlshortener.utils.UrlValidator;

import java.util.List;

public class ShortUrlServiceImpl implements ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final Crockford32Encoder encoder;

    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository, Crockford32Encoder encoder) {
        this.shortUrlRepository = shortUrlRepository;
        this.encoder = encoder;
    }

    @Override
    public ShortUrlDto getShortCode(String shortCode) {
        var id = encoder.decode(shortCode);
        var shortUrl = shortUrlRepository.findById(id);
        if (shortUrl.isEmpty()) {
            throw new CodeNotFoundException("Short code not found: " + shortCode);
        }
        return mapToDto(shortUrl.get());
    }

    @Override
    public ShortUrlDto getOrCreateShortCode(String url) {
        url = url.trim();
        if (!UrlValidator.isValidUrl(url)) {
            throw new FormatException("Invalid URL: " + url);
        }
        var shortUrl = shortUrlRepository.getOrCreateShortUrl(url);
        return mapToDto(shortUrl);
    }

    @Override
    public List<ShortUrlDto> getShortCodes() {
        return shortUrlRepository
                .getShortCodes()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private ShortUrlDto mapToDto(ShortUrl shortUrl) {
        return new ShortUrlDto(shortUrl.getId(), encoder.encode(shortUrl.getId()), shortUrl.getOriginalUrl());
    }
}
