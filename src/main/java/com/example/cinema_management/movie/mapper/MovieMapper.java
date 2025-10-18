package com.example.cinema_management.movie.mapper;


import com.example.cinema_management.movie.dto.*;
import com.example.cinema_management.movie.entity.Movie;


public class MovieMapper {
    public static MovieResponse toResponse(Movie m, String posterBaseUrl) {
        String url = null;
        if (m.getPosterPath() != null && !m.getPosterPath().isBlank()) {
            String p = m.getPosterPath();
            if (p.startsWith("http://") || p.startsWith("https://")) {
                url = p; // remote URL
            } else {
                url = posterBaseUrl + "/" + p; // local path
            }
        }
        return new MovieResponse(
                m.getId(), m.getTitle(), m.getDescription(), m.getGenre(), m.getLanguage(),
                m.getDurationMinutes(), m.getReleaseDate(), m.getRating(), url, m.isActive()
        );
    }


    public static void apply(Movie m, MovieCreateRequest r) {
        m.setTitle(r.title());
        m.setDescription(r.description());
        m.setGenre(r.genre());
        m.setLanguage(r.language());
        m.setDurationMinutes(r.durationMinutes());
        m.setReleaseDate(r.releaseDate());
        m.setRating(r.rating());
        if (r.active() != null) m.setActive(r.active());
    }


    public static void apply(Movie m, MovieUpdateRequest r) {
        m.setTitle(r.title());
        m.setDescription(r.description());
        m.setGenre(r.genre());
        m.setLanguage(r.language());
        m.setDurationMinutes(r.durationMinutes());
        m.setReleaseDate(r.releaseDate());
        m.setRating(r.rating());
        if (r.active() != null) m.setActive(r.active());
    }
}