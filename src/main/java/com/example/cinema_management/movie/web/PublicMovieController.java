package com.example.cinema_management.movie.web;


import com.example.cinema_management.movie.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/movies")
public class PublicMovieController {
    private final MovieService svc;
    public PublicMovieController(MovieService svc) { this.svc = svc; }


    @GetMapping
    public String list(@RequestParam(required=false) String q,
                       @RequestParam(defaultValue="0") int page,
                       Model model) {
        model.addAttribute("page", svc.searchPublic(q, page, 12));
        model.addAttribute("q", q);
        return "public/movies/list";
    }


    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("movie", svc.get(id));
        return "public/movies/details";
    }

    @GetMapping("/tmdb")
    public String redirectTmdbAdmin() {
        return "redirect:/admin/movies/tmdb";
    }
}