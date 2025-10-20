package com.example.cinema_management.movie.service;


import com.example.cinema_management.movie.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;


public interface MovieService {
    Page<MovieResponse> searchPublic(String q, int page, int size);
    Page<MovieResponse> searchAdmin(String q, int page, int size);
    MovieResponse get(Long id);
    MovieResponse create(MovieCreateRequest req, MultipartFile poster);
    MovieResponse update(Long id, MovieUpdateRequest req, MultipartFile newPoster);
    void delete(Long id);
}
