package com.example.urlshortener.services.encoding.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class Crockford32EncoderImplTest {

    private final Crockford32EncoderImpl underTest = new Crockford32EncoderImpl();

    @ParameterizedTest
    @CsvSource({"1234567890, 14sc0pj",
            "123456789, 3nqk8n",
            "6425673, 64329",
            "3224, 34r"})
    void whenEncode_withKnownValues_returnsExpectedLong(Long input, String expected) {
        var encoded = underTest.encode(input);
        Assertions.assertEquals(expected, encoded);
    }

    @ParameterizedTest
    @CsvSource({"64329, 6425673",
            "34r, 3224"})
    void whenDecode_withKnownValues_returnsExpectedString(String input, Long expected) {
        var decoded = underTest.decode(input);
        Assertions.assertEquals(expected, decoded);
    }
}