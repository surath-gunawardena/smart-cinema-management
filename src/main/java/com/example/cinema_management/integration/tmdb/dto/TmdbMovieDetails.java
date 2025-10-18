package com.example.cinema_management.integration.tmdb.dto;


import java.util.List;


public record TmdbMovieDetails(long id, String title, String overview, int runtime,
                               String release_date, String original_language, String poster_path,
                               List<Genre> genres) {
    public record Genre(int id, String name) {}
}


