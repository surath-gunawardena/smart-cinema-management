package com.example.cinema_management.movie.web;


import com.example.cinema_management.movie.dto.*;
import com.example.cinema_management.movie.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/admin/movies")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMovieController {
    private final MovieService svc;
    public AdminMovieController(MovieService svc) { this.svc = svc; }


    @GetMapping
    public String list(@RequestParam(required=false) String q,
                       @RequestParam(defaultValue="0") int page,
                       Model model) {
        model.addAttribute("page", svc.searchAdmin(q, page, 20));
        model.addAttribute("q", q);
        return "admin/movies/list";
    }


    @GetMapping("/new")
    public String formCreate(Model model) {
        model.addAttribute("movie", new MovieCreateRequest("", "", "", "", null, null, "U", true));
        return "admin/movies/form";
    }


    @PostMapping
    public String handleCreate(@ModelAttribute("movie") @Valid MovieCreateRequest req,
                               @RequestParam(value="poster", required=false) MultipartFile poster) {
        svc.create(req, poster);
        return "redirect:/admin/movies";
    }


    @GetMapping("/{id}/edit")
    public String formEdit(@PathVariable Long id, Model model) {
        model.addAttribute("movieId", id);
        model.addAttribute("movieResp", svc.get(id));
        return "admin/movies/form";
    }


    @PostMapping("/{id}")
    public String handleUpdate(@PathVariable Long id,
                               @ModelAttribute("movie") @Valid MovieUpdateRequest req,
                               @RequestParam(value="poster", required=false) MultipartFile poster) {
        svc.update(id, req, poster);
        return "redirect:/admin/movies";
    }


    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        svc.delete(id);
        return "redirect:/admin/movies";
    }

    @GetMapping("/tmdb")
    public String tmdbPage() { return "admin/movies/tmdb"; }
}