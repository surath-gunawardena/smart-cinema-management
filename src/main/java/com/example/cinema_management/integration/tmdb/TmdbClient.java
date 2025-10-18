package com.example.cinema_management.integration.tmdb;


import com.example.cinema_management.integration.tmdb.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class TmdbClient {
    private final RestClient http;
    private final String imageSize;
    private final TmdbConfigCache configCache;


    public TmdbClient(@Value("${tmdb.api-base-url}") String baseUrl,
                      @Value("${tmdb.bearer-token}") String bearerToken,
                      @Value("${tmdb.image-size:w500}") String imageSize,
                      TmdbConfigCache configCache) {
        this.http = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .build();
        this.imageSize = imageSize;
        this.configCache = configCache;
    }


    public TmdbSearchResponse searchMovies(String query, int page, String language) {
        return http.get().uri(uri -> uri.path("/search/movie")
                        .queryParam("query", query)
                        .queryParam("page", page)
                        .queryParam("language", language)
                        .build())
                .retrieve().body(TmdbSearchResponse.class);
    }


    public TmdbMovieDetails getMovie(long id, String language) {
        return http.get().uri(uri -> uri.path("/movie/{id}")
                        .queryParam("language", language)
                        .build(id))
                .retrieve().body(TmdbMovieDetails.class);
    }


    public TmdbReleaseDatesResponse getReleaseDates(long id) {
        return http.get().uri("/movie/{id}/release_dates", id)
                .retrieve().body(TmdbReleaseDatesResponse.class);
    }


    public String buildPosterUrl(String posterPath) {
        if (posterPath == null || posterPath.isBlank()) return null;
        TmdbConfiguration cfg = configCache.get();
        if (cfg == null || cfg.images() == null || cfg.images().secure_base_url() == null) return null;
        return cfg.images().secure_base_url() + imageSize + posterPath;
    }
}