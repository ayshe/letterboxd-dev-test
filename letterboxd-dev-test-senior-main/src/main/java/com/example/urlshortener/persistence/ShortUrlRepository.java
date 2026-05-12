package com.example.urlshortener.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class ShortUrlRepository {
    private final EntityManagerFactory emf;

    public ShortUrlRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Optional<ShortUrl> findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(ShortUrl.class, id));
        }
    }

    public List<ShortUrl> getShortCodes() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<ShortUrl> query = em.createQuery("SELECT s FROM ShortUrl s", ShortUrl.class);
            return query.getResultList();
        }
    }

    public ShortUrl getOrCreateShortUrl(String url) {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<ShortUrl> query = em.createQuery(
                    "SELECT s FROM ShortUrl s WHERE s.originalUrl = :url",
                    ShortUrl.class
            );
            query.setParameter("url", url);

            ShortUrl existing = query.getResultStream().findFirst().orElse(null);
            if (existing != null) {
                System.out.println("Found existing short URL with ID:" + existing.getId() + " for URL: " + url);
                return existing;
            }

            em.getTransaction().begin();

            ShortUrl shortUrl = new ShortUrl(url);
            em.persist(shortUrl);

            em.getTransaction().commit();
            System.out.println("Created short URL with ID:" + shortUrl.getId() + " for URL: " + url);

            return shortUrl;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }
}
