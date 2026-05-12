package com.example.urlshortener.services.encoding.impl;

import com.example.urlshortener.services.encoding.Crockford32Encoder;
import com.msiops.ground.crockford32.Crockford32;

public class Crockford32EncoderImpl implements Crockford32Encoder {

    @Override
    public String encode(Long id) {
        return Crockford32.encode(id).toLowerCase();
    }

    @Override
    public Long decode(String encodedId) {
        return Crockford32.decode(encodedId.toUpperCase()).longValue();
    }
}
