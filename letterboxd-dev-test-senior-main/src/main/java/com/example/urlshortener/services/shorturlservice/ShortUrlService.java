package com.example.urlshortener.services.shorturlservice;

import com.example.urlshortener.dto.ShortUrlDto;

import java.util.List;

public interface ShortUrlService {
    ShortUrlDto getShortCode(String url);

    List<ShortUrlDto> getShortCodes();
}
