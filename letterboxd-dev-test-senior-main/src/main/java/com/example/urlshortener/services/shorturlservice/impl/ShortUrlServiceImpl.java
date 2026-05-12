package com.example.urlshortener.services.shorturlservice.impl;

import com.example.urlshortener.dto.ShortUrlDto;
import com.example.urlshortener.persistence.ShortUrl;
import com.example.urlshortener.persistence.ShortUrlRepository;
import com.example.urlshortener.services.encoding.Crockford32Encoder;
import com.example.urlshortener.services.shorturlservice.ShortUrlService;

import java.util.List;

public class ShortUrlServiceImpl implements ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final Crockford32Encoder encoder;

    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository, Crockford32Encoder encoder) {
        this.shortUrlRepository = shortUrlRepository;
        this.encoder = encoder;
    }

    @Override
    public ShortUrlDto getShortCode(String url) {
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
