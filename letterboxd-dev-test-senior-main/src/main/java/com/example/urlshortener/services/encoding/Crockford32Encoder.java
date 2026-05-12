package com.example.urlshortener.services.encoding;

public interface Crockford32Encoder {
    String encode(Long id);

    Long decode(String encodedId);
}
