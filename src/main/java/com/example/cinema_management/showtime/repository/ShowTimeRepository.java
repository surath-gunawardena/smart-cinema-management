package com.example.cinema_management.showtime.repository;

import com.example.cinema_management.showtime.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {
    boolean existsByMovieId(Long movieId);
}
