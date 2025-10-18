package com.example.cinema_management.movie.repository;


import com.example.cinema_management.movie.entity.Movie;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findByTitleContainingIgnoreCaseAndActiveTrue(String q, Pageable pageable);
    boolean existsByTitleIgnoreCase(String title);
}

