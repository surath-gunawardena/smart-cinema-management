package com.example.cinema_management.integration.tmdb.dto;


import java.util.List;


public record TmdbSearchResponse(int page, List<Result> results, int total_pages, int total_results) {
    public record Result(long id, String title, String original_title, String release_date,
                         String overview, String original_language, String poster_path) {}
}