package com.example.urlshortener;

import com.example.urlshortener.exception.CodeNotFoundException;
import com.example.urlshortener.exception.FormatException;
import com.example.urlshortener.services.shorturlservice.ShortUrlService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RedirectServlet extends HttpServlet {
    private final ShortUrlService shortUrlService;

    public RedirectServlet(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        String path = request.getPathInfo();

        try {
            if (path != null && path.length() > 1) {
                resolveShortCode(request.getPathInfo().substring(1), response);
            }
        } catch (CodeNotFoundException e) {
            response.sendRedirect("/");
        } catch (FormatException e) {
            response.sendError(400, e.getMessage());
        } catch (Exception e) {
            response.sendError(500, "Internal Server Error");
        }
    }

    private void resolveShortCode(String shortCode, HttpServletResponse response) throws IOException {
        var dto = shortUrlService.getShortCode(shortCode);
        response.sendRedirect(dto.originalUrl());
    }
}