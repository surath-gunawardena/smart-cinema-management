package com.example.cinema_management.pricing;

import com.example.cinema_management.movie.MovieRepository;
import com.example.cinema_management.pricing.dto.PricingBulkRequest;
import com.example.cinema_management.pricing.dto.PricingShowtimeRequest;
import com.example.cinema_management.showtime.ShowTimeRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/pricing")
public class AdminPricingController {

    private final PricingService pricingService;
    private final MovieRepository movieRepo;
    private final ShowTimeRepository showTimeRepo;

    public AdminPricingController(PricingService svc, MovieRepository m, ShowTimeRepository s) {
        this.pricingService = svc; this.movieRepo = m; this.showTimeRepo = s;
    }

    @GetMapping()
    public String index(Model model) {
        return "admin/pricing_management";
    }

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) Integer screenId,
                       @RequestParam(required = false) SeatType seatType,
                       Model model) {
        var pageable = org.springframework.data.domain.PageRequest.of(page, size);
        var pager = pricingService.pageAll(screenId, seatType, pageable);
        model.addAttribute("pageTitle", "Pricing");
        model.addAttribute("pager", pager);
        model.addAttribute("screenId", screenId);
        model.addAttribute("seatType", seatType);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "admin/pricing-list"; // must match the file you just created
    }

    @GetMapping("/showtime/{showTimeId}/json")
    @ResponseBody
    public Map<String, Object> pricesForShowtime(@PathVariable Long showTimeId) {
        var map = new HashMap<String, Object>();
        var zero = java.math.BigDecimal.ZERO;
        var prices = new HashMap<String, BigDecimal>();
        prices.put("ADULT", zero); prices.put("CHILD", zero);
        prices.put("SENIOR", zero); prices.put("STUDENT", zero);
        pricingService.listForShowtime(showTimeId).forEach(p -> prices.put(p.getSeatType().name(), p.getPrice()));
        map.put("showTimeId", showTimeId);
        map.put("prices", prices);
        return map;
    }

    // --- SAVE: create/update all seat types for a showtime
    @PostMapping("/showtime/{showTimeId}/bulk")
    public String saveShowtime(@PathVariable Long showTimeId,
                               @Valid PricingShowtimeRequest req,
                               BindingResult br,
                               RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("error", "Please fix validation errors.");
            return "redirect:/admin/pricing";
        }
        req.setShowTimeId(showTimeId);
        pricingService.upsertForShowtime(req);
        ra.addFlashAttribute("success", "Pricing saved for showtime " + showTimeId);
        return "redirect:/admin/pricing";
    }

}
