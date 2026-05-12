package com.example.urlshortener;

import com.example.urlshortener.dto.ShortUrlDto;
import com.example.urlshortener.services.shorturlservice.ShortUrlService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class UrlShortenerServlet extends HttpServlet {
    private final ShortUrlService shortUrlService;

    public UrlShortenerServlet(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        var dto = shortUrlService.getShortCode(request.getRequestURL().toString());
        response.getWriter().write("<p>Hello from URL Shortener</p>");
        writeCodesTable(response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("<p>Hello from URL Shortener</p>");
    }

    private void writeCodesTable(PrintWriter writer) {
        writer.write("<table>");
        writer.write("<th>Id</th><th>ShortCode</th><th>OriginalUrl</th>");
        for (var row : shortUrlService.getShortCodes()
        ) {
            writer.write("<tr><td>" + row.id() + "</td><td>" + getShortCodeHyperlink(row) + "</td><td>" + row.originalUrl() + "</td></tr>");

        }
        writer.write("</table>");
    }

    private String getShortCodeHyperlink(ShortUrlDto shortUrlDto) {
        return "<a href='" + shortUrlDto.shortCode() + "'>" + shortUrlDto.shortCode() + "</a>";
    }

}
