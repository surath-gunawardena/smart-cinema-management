package com.example.cinema_management.screen.repository;

import com.example.cinema_management.screen.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {
    boolean existsByMovieId(Long movieId);
}
