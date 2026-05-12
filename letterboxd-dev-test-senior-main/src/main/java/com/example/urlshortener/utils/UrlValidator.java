package com.example.urlshortener.utils;

public class UrlValidator {
    public static boolean isValidUrl(String url) {
        return url != null && url.matches("https?://.+");
    }
}
