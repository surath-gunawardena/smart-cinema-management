package com.example.cinema_management.integration.tmdb.dto;


import java.util.List;


public record TmdbReleaseDatesResponse(List<Result> results) {
    public record Result(String iso_3166_1, List<ReleaseDate> release_dates) {}
    public record ReleaseDate(String certification, String type, String note) {}
}