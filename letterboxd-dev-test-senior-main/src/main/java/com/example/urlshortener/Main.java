package com.example.urlshortener;

import com.example.urlshortener.persistence.ShortUrlRepository;
import com.example.urlshortener.services.encoding.Crockford32Encoder;
import com.example.urlshortener.services.encoding.impl.Crockford32EncoderImpl;
import com.example.urlshortener.services.shorturlservice.ShortUrlService;
import com.example.urlshortener.services.shorturlservice.impl.ShortUrlServiceImpl;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        final int port = 8080;

        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        final Context context = tomcat.addContext("", new File(".").getAbsolutePath());

        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("url-shortener-pu");
        final Crockford32Encoder encoder = new Crockford32EncoderImpl();
        final ShortUrlRepository shortUrlRepository = new ShortUrlRepository(emf);
        final ShortUrlService shortUrlService = new ShortUrlServiceImpl(shortUrlRepository, encoder);

        Tomcat.addServlet(context, "urlShortener", new UrlShortenerServlet(shortUrlService));
        context.addServletMappingDecoded("/*", "urlShortener");

        tomcat.start();
        System.out.println("Server started on http://localhost:" + port);
        tomcat.getServer().await();
    }

}
