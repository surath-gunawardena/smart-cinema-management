package com.example.cinema_management.integration.tmdb.dto;


public record TmdbConfiguration(Images images) {
    public record Images(String base_url, String secure_base_url, String[] poster_sizes) {}
}
