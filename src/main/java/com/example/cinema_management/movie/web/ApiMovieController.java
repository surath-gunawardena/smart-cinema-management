package com.example.cinema_management.movie.web;


import com.example.cinema_management.movie.dto.*;
import com.example.cinema_management.movie.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api")
public class ApiMovieController {
    private final MovieService svc;
    public ApiMovieController(MovieService svc) { this.svc = svc; }


    @GetMapping("/movies")
    public Page<MovieResponse> browse(@RequestParam(required = false) String q,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "12") int size) {
        return svc.searchPublic(q, page, size);
    }


    @GetMapping("/movies/{id}")
    public MovieResponse details(@PathVariable Long id) { return svc.get(id); }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/movies")
    public Page<MovieResponse> adminList(@RequestParam(required = false) String q,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        return svc.searchAdmin(q, page, size);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/admin/movies", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MovieResponse create(@Valid @RequestPart("data") MovieCreateRequest req,
                                @RequestPart(value = "poster", required = false) MultipartFile poster) {
        return svc.create(req, poster);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/admin/movies/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MovieResponse update(@PathVariable Long id,
                                @Valid @RequestPart("data") MovieUpdateRequest req,
                                @RequestPart(value = "poster", required = false) MultipartFile poster) {
        return svc.update(id, req, poster);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/movies/{id}")
    public void delete(@PathVariable Long id) { svc.delete(id); }
}