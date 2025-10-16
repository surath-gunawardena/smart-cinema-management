package com.example.cinema_management.pricing;

import com.example.cinema_management.pricing.dto.PricingBulkRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pricing")
public class AdminPricingController {

    private final PricingService service;

    public AdminPricingController(PricingService service) { this.service = service; }

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
        var pager = service.pageAll(screenId, seatType, pageable);
        model.addAttribute("pageTitle", "Pricing");
        model.addAttribute("pager", pager);
        model.addAttribute("screenId", screenId);
        model.addAttribute("seatType", seatType);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "admin/pricing-list"; // must match the file you just created
    }

    // existing endpoints you already had
    @GetMapping("/screen/{screenId}")
    public String screenPricing(@PathVariable Integer screenId, Model model) {
        model.addAttribute("pageTitle", "Pricing - Screen " + screenId);
        model.addAttribute("screenId", screenId);
        model.addAttribute("prices", service.listForScreen(screenId));
        return "admin/pricing-screen";
    }

    @PostMapping("/screen/{screenId}/bulk")
    public String upsert(@PathVariable Integer screenId,
                         @Valid PricingBulkRequest req,
                         RedirectAttributes ra) {
        req.setScreenId(screenId);
        service.upsertForScreen(req);
        ra.addFlashAttribute("success", "Pricing updated for screen " + screenId);
        return "redirect:/admin/pricing/screen/" + screenId;
    }

    @PostMapping("/screen/{screenId}/delete/{seatType}")
    public String deleteType(@PathVariable Integer screenId,
                             @PathVariable SeatType seatType,
                             RedirectAttributes ra) {
        service.deleteForScreenAndType(screenId, seatType);
        ra.addFlashAttribute("info", seatType + " price removed.");
        return "redirect:/admin/pricing/screen/" + screenId;
    }
}
