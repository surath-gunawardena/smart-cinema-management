package com.example.cinema_management.movie.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;


@Entity
@Table(name = "movies", indexes = {@Index(name = "ix_movies_title", columnList = "title")})
public class Movie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank @Size(max = 200)
    private String title;


    @Size(max = 1000)
    private String description;


    @Size(max = 50)
    private String genre;


    @Size(max = 20)
    private String language;


    @Positive
    private Integer durationMinutes;


    private LocalDate releaseDate;


    @Pattern(regexp = "G|PG|PG-13|R|NC-17|U|UA|A|\\w{1,10}")
    @Column(length = 10)
    private String rating;


    @Column(length = 400)
    private String posterPath; // local filename or absolute URL


    @Column(nullable = false)
    private boolean active = true;


    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
