package com.example.cinema_management.showtime;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {

    List<ShowTime> findByMovieIdOrderByDateAscStartTimeAsc(Long movieId);
}
