package com.example.cinema_management.movie.web;


import com.example.cinema_management.integration.tmdb.TmdbClient;
import com.example.cinema_management.integration.tmdb.dto.TmdbSearchResponse;
import com.example.cinema_management.movie.dto.MovieResponse;
import com.example.cinema_management.movie.service.MovieImportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/tmdb")
@PreAuthorize("hasRole('ADMIN')")
public class ApiTmdbController {
    private final TmdbClient tmdb;
    private final MovieImportService importer;
    private final String defaultLanguage;
    private final String defaultRegion;


    public ApiTmdbController(TmdbClient tmdb,
                             MovieImportService importer,
                             @Value("${tmdb.default-language:en-US}") String defaultLanguage,
                             @Value("${tmdb.default-region:US}") String defaultRegion) {
        this.tmdb = tmdb; this.importer = importer; this.defaultLanguage = defaultLanguage; this.defaultRegion = defaultRegion;
    }


    @GetMapping("/search")
    public TmdbSearchResponse search(@RequestParam String q,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(required = false) String language) {
        return tmdb.searchMovies(q, page, language != null ? language : defaultLanguage);
    }


    @PostMapping("/{tmdbId}/import")
    public MovieResponse importOne(@PathVariable long tmdbId,
                                   @RequestParam(required = false) String language,
                                   @RequestParam(required = false) String region) {
        return importer.importFromTmdb(
                tmdbId,
                language != null ? language : defaultLanguage,
                region != null ? region : defaultRegion
        );
    }
}