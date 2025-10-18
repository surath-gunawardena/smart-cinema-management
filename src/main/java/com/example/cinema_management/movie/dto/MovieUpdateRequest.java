package com.example.cinema_management.movie.dto;


import jakarta.validation.constraints.*;
import java.time.LocalDate;


public record MovieUpdateRequest(
        @NotBlank @Size(max = 200) String title,
        @Size(max = 1000) String description,
        @Size(max = 50) String genre,
        @Size(max = 20) String language,
        @Positive Integer durationMinutes,
        LocalDate releaseDate,
        @Pattern(regexp = "G|PG|PG-13|R|NC-17|U|UA|A|\\w{1,10}") String rating,
        Boolean active
) {}