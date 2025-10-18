package com.example.cinema_management.integration.tmdb;


import com.example.cinema_management.integration.tmdb.dto.TmdbConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


import java.time.Instant;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;


@Component
public class TmdbConfigCache {
    private final RestClient http;
    private final AtomicReference<Cached> cache = new AtomicReference<>();
    private final Duration ttl = Duration.ofHours(12);


    public TmdbConfigCache(@Value("${tmdb.api-base-url}") String baseUrl,
                           @Value("${tmdb.bearer-token}") String bearerToken) {
        this.http = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .build();
    }


    public TmdbConfiguration get() {
        Cached c = cache.get();
        if (c != null && Instant.now().isBefore(c.expiresAt)) {
            return c.data;
        }
        TmdbConfiguration fresh = http.get().uri("/configuration").retrieve().body(TmdbConfiguration.class);
        if (fresh != null) {
            cache.set(new Cached(fresh, Instant.now().plus(ttl)));
        }
        return fresh;
    }


    private record Cached(TmdbConfiguration data, Instant expiresAt) {}
}