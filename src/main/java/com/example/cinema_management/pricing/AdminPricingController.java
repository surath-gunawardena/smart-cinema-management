package com.example.cinema_management.pricing;

import com.example.cinema_management.pricing.dto.PricingCreateRequest;
import com.example.cinema_management.pricing.dto.PricingForm;
import com.example.cinema_management.pricing.dto.PricingUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/pricing")
public class AdminPricingController {

    private final PricingService service;

    public AdminPricingController(PricingService service) {
        this.service = service;
    }

    // List page (pagination)
    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        Page<Pricing> pg = service.page(PageRequest.of(page, size));
        model.addAttribute("pageTitle", "Pricing");
        model.addAttribute("page", pg);
        return "admin/pricing-list";
    }

    // Create form
    @GetMapping("/new")
    public String createForm(Model model) {
        var form = new PricingForm();
        form.setName("");
        form.setAdultPrice(new BigDecimal("0.00"));
        form.setChildPrice(new BigDecimal("0.00"));

        model.addAttribute("pageTitle", "Create Pricing");
        model.addAttribute("form", form);
        model.addAttribute("mode", "create");

        return "admin/pricing-form";
    }

    // Create submit
    @PostMapping
    public String createSubmit(@Valid @ModelAttribute("form") PricingForm form,
                               BindingResult binding,
                               RedirectAttributes ra,
                               Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("pageTitle", "Create Pricing");
            model.addAttribute("mode", "create");
            return "admin/pricing-form";
        }
        var req = new PricingCreateRequest();
        req.name = form.getName();
        req.prices = new EnumMap<>(SeatType.class);
        req.prices.put(SeatType.ADULT, form.getAdultPrice());
        req.prices.put(SeatType.CHILD, form.getChildPrice());

        try {
            Long id = service.create(req);
            ra.addFlashAttribute("success", "Pricing created.");
            return "redirect:/admin/pricing/" + id + "/edit";
        } catch (IllegalArgumentException ex) {
            binding.rejectValue("name", "name.exists", ex.getMessage());
            model.addAttribute("pageTitle", "Create Pricing");
            model.addAttribute("mode", "create");
            return "admin/pricing-form";
        }
    }

    // Edit form
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var p = service.getOrThrow(id);
        var types = service.listTypes(id);
        Map<SeatType, BigDecimal> map = new EnumMap<>(SeatType.class);
        types.forEach(t -> map.put(t.getType(), t.getPrice()));

        var form = new PricingForm();
        form.setId(p.getId());
        form.setName(p.getName());
        form.setAdultPrice(map.getOrDefault(SeatType.ADULT, new BigDecimal("0.00")));
        form.setChildPrice(map.getOrDefault(SeatType.CHILD, new BigDecimal("0.00")));

        model.addAttribute("pageTitle", "Edit Pricing");
        model.addAttribute("form", form);
        model.addAttribute("mode", "edit");
        return "admin/pricing-form";
    }

    // Edit submit
    @PostMapping("/{id}")
    public String editSubmit(@PathVariable Long id,
                             @Valid @ModelAttribute("form") PricingForm form,
                             BindingResult binding,
                             RedirectAttributes ra,
                             Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Pricing");
            model.addAttribute("mode", "edit");
            return "admin/pricing-form";
        }
        var req = new PricingUpdateRequest();
        req.name = form.getName();
        req.prices = new EnumMap<>(SeatType.class);
        req.prices.put(SeatType.ADULT, form.getAdultPrice());
        req.prices.put(SeatType.CHILD, form.getChildPrice());

        try {
            service.update(id, req);
            ra.addFlashAttribute("success", "Pricing updated.");
            return "redirect:/admin/pricing";
        } catch (IllegalArgumentException ex) {
            binding.rejectValue("name", "name.exists", ex.getMessage());
            model.addAttribute("pageTitle", "Edit Pricing");
            model.addAttribute("mode", "edit");
            return "admin/pricing-form";
        }
    }

    // Delete
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("info", "Pricing deleted.");
        return "redirect:/admin/pricing";
    }
}