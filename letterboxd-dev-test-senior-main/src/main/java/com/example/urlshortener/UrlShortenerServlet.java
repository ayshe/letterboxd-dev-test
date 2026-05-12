package com.example.urlshortener;

import com.example.urlshortener.dto.ShortUrlDto;
import com.example.urlshortener.exception.FormatException;
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

        response.getWriter().write("<p>Hello from URL Shortener</p>");

        writeSubmissionForm(response.getWriter());
        writeCodesTable(response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = request.getParameter("url");

        try {
            if (url == null || url.isBlank()) {
                response.sendError(400, "URL is required");
                return;
            }
            var shortCode = shortUrlService.getOrCreateShortCode(url);

            response.getWriter().write("<p>Your short URL for " + url + " is " + getShortCodeHyperlink(shortCode) + "</p>");
            response.getWriter().write("<a href='/'>Shorten another URL</a>");

            writeCodesTable(response.getWriter());
        } catch (FormatException e) {
            response.sendError(400, e.getMessage());
        } catch (Exception e) {
            response.sendError(500, "Internal Server Error");
        }
    }

    private void writeSubmissionForm(PrintWriter writer) {
        writer.write("""
                <form method="POST" action="/">
                    <label for="url">Enter URL:</label><br/>
                    <input type="text" id="url" name="url" />
                    <br/><br/>
                    <button type="submit">Shorten</button>
                </form>
                """);
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
        return "<a href='/r/" + shortUrlDto.shortCode() + "' target='_blank'>/r/" + shortUrlDto.shortCode() + "</a>";
    }

}
