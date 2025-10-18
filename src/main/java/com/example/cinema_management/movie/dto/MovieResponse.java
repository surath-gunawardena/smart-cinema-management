package com.example.cinema_management.movie.dto;


import java.time.LocalDate;


public record MovieResponse(
        Long id,
        String title,
        String description,
        String genre,
        String language,
        Integer durationMinutes,
        LocalDate releaseDate,
        String rating,
        String posterUrl,
        boolean active
) {}