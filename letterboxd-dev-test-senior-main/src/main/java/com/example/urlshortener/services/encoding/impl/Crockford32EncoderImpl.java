package com.example.urlshortener.services.encoding.impl;

import com.example.urlshortener.exception.FormatException;
import com.example.urlshortener.services.encoding.Crockford32Encoder;
import com.msiops.ground.crockford32.Crockford32;

public class Crockford32EncoderImpl implements Crockford32Encoder {

    @Override
    public String encode(Long id) {
        if (id == null || id < 0) {
            throw new InternalError("Invalid id provided");
        }

        return Crockford32.encode(id).toLowerCase();
    }

    @Override
    public Long decode(String encodedId) {
        if (encodedId == null || encodedId.isBlank()) {
            throw new FormatException("No code provided");
        }

        try {
            return Crockford32.decode(encodedId.toUpperCase()).longValue();
        } catch (Exception e) {
            throw new FormatException("Invalid short code or decode failure: " + encodedId);
        }
    }
}
