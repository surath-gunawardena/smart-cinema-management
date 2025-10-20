package com.example.cinema_management.movie.service;

import com.example.cinema_management.integration.tmdb.TmdbClient;
import com.example.cinema_management.integration.tmdb.dto.TmdbMovieDetails;
import com.example.cinema_management.integration.tmdb.dto.TmdbReleaseDatesResponse;
import com.example.cinema_management.movie.dto.MovieCreateRequest;
import com.example.cinema_management.movie.dto.MovieResponse;
import com.example.cinema_management.movie.entity.Movie;
import com.example.cinema_management.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieImportServiceImpl implements MovieImportService {

    private final TmdbClient tmdb;
    private final MovieRepository movieRepo;
    private final MovieService movieService;
    private final PosterStorageService posterStorage; // kept for optional downloads
    private final boolean downloadPosters;
    private final String defaultLanguage;
    private final String defaultRegion;

    public MovieImportServiceImpl(
            TmdbClient tmdb,
            MovieRepository movieRepo,
            MovieService movieService,
            PosterStorageService posterStorage,
            @Value("${tmdb.download-posters:false}") boolean downloadPosters,
            @Value("${tmdb.default-language:en-US}") String defaultLanguage,
            @Value("${tmdb.default-region:US}") String defaultRegion) {

        this.tmdb = tmdb;
        this.movieRepo = movieRepo;
        this.movieService = movieService;
        this.posterStorage = posterStorage;
        this.downloadPosters = downloadPosters;
        this.defaultLanguage = defaultLanguage;
        this.defaultRegion = defaultRegion;
    }

    @Override
    public MovieResponse importFromTmdb(long tmdbId, String language, String region) {
        String lang = (language == null) ? defaultLanguage : language;
        String reg  = (region   == null) ? defaultRegion   : region;

        TmdbMovieDetails details = tmdb.getMovie(tmdbId, lang);
        if (details == null) throw new IllegalArgumentException("TMDB movie not found: " + tmdbId);

        String rating = findCertification(tmdbId, reg);
        String genres = (details.genres() == null) ? null :
                details.genres().stream().map(TmdbMovieDetails.Genre::name).collect(Collectors.joining(", "));
        LocalDate release = (details.release_date() == null || details.release_date().isBlank())
                ? null : LocalDate.parse(details.release_date());

        MovieCreateRequest req = new MovieCreateRequest(
                details.title(), details.overview(), genres, details.original_language(),
                (details.runtime() > 0 ? details.runtime() : null), release,
                (rating == null || rating.isBlank()) ? "U" : rating, true
        );

        MovieResponse created = movieService.create(req, null);

        String remotePosterUrl = tmdb.buildPosterUrl(details.poster_path());
        if (remotePosterUrl != null && !downloadPosters) {
            Movie m = movieRepo.findById(created.id()).orElseThrow();
            m.setPosterPath(remotePosterUrl);
            movieRepo.save(m);
        }
        return movieService.get(created.id());
    }

    private String findCertification(long tmdbId, String region) {
        TmdbReleaseDatesResponse rel = tmdb.getReleaseDates(tmdbId);
        if (rel == null || rel.results() == null) return null;
        return rel.results().stream()
                .filter(r -> region.equalsIgnoreCase(r.iso_3166_1()))
                .findFirst()
                .flatMap(r -> r.release_dates().stream()
                        .map(TmdbReleaseDatesResponse.ReleaseDate::certification)
                        .filter(s -> s != null && !s.isBlank())
                        .findFirst())
                .orElse(null);
    }
}
