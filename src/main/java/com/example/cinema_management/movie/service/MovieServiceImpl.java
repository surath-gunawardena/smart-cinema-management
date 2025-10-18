// src/main/java/com/example/cinema_management/movie/service/MovieServiceImpl.java
package com.example.cinema_management.movie.service;

import com.example.cinema_management.movie.dto.MovieCreateRequest;
import com.example.cinema_management.movie.dto.MovieResponse;
import com.example.cinema_management.movie.dto.MovieUpdateRequest;
import com.example.cinema_management.movie.entity.Movie;
import com.example.cinema_management.movie.mapper.MovieMapper;
import com.example.cinema_management.movie.repository.MovieRepository;
import com.example.cinema_management.screen.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository repo;
    private final ShowTimeRepository showTimes;
    private final PosterStorageService storage;
    private final String posterBaseUrl;

    public MovieServiceImpl(
            MovieRepository repo,
            ShowTimeRepository showTimes,
            PosterStorageService storage,
            @Value("${app.media.posters-url-base:/media/posters}") String posterBaseUrl) {
        this.repo = repo;
        this.showTimes = showTimes;
        this.storage = storage;
        this.posterBaseUrl = posterBaseUrl;
    }

    // map entity -> response with proper poster URL handling
    private MovieResponse toResp(Movie m) {
        return MovieMapper.toResponse(m, posterBaseUrl);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovieResponse> searchPublic(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return repo
                .findByTitleContainingIgnoreCaseAndActiveTrue(q == null ? "" : q, pageable)
                .map(this::toResp);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovieResponse> searchAdmin(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        if (q == null || q.isBlank()) {
            return repo.findAll(pageable).map(this::toResp);
        }
        // Query-by-Example on title (contains, ignore case)
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("title", GenericPropertyMatchers.contains().ignoreCase())
                // ignore fields we’re not filtering on
                .withIgnorePaths("active", "durationMinutes", "id", "posterPath", "rating");
        Movie probe = new Movie();
        probe.setTitle(q);
        return repo.findAll(Example.of(probe, matcher), pageable).map(this::toResp);
    }

    @Override
    @Transactional(readOnly = true)
    public MovieResponse get(Long id) {
        return repo.findById(id)
                .map(this::toResp)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
    }

    @Override
    public MovieResponse create(MovieCreateRequest req, MultipartFile poster) {
        if (repo.existsByTitleIgnoreCase(req.title())) {
            throw new IllegalArgumentException("Movie title already exists");
        }
        Movie m = new Movie();
        MovieMapper.apply(m, req);
        m = repo.save(m);
        String posterPath = storage.storePoster(poster, m.getId());
        if (posterPath != null) {
            m.setPosterPath(posterPath);
        }
        // entity is managed; changes will flush automatically
        return toResp(m);
    }

    @Override
    public MovieResponse update(Long id, MovieUpdateRequest req, MultipartFile newPoster) {
        Movie m = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        String old = m.getPosterPath();
        MovieMapper.apply(m, req);
        if (newPoster != null && !newPoster.isEmpty()) {
            String path = storage.storePoster(newPoster, m.getId());
            m.setPosterPath(path);
            storage.deletePosterIfExists(old);
        }
        return toResp(m);
    }

    @Override
    public void delete(Long id) {
        Movie m = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        if (showTimes.existsByMovieId(id)) {
            throw new IllegalStateException("Cannot delete - Movie has scheduled showtimes");
        }
        repo.delete(m);
        storage.deletePosterIfExists(m.getPosterPath());
    }
}
