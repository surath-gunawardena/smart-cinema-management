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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/pricing")
public class AdminPricingController {

    private final PricingService service;
    private final PricingRepository pricingRepository;
    private final PricingTypeRepository pricingTypeRepository;

    public AdminPricingController(PricingService service, PricingRepository pricingRepository1, PricingTypeRepository pricingTypeRepository) {
        this.service = service;
        this.pricingRepository = pricingRepository1;
        this.pricingTypeRepository = pricingTypeRepository;
    }

    @GetMapping()
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Page<Pricing> pg = pricingRepository.findAll(PageRequest.of(page, size));

        Map<Long, Map<SeatType, BigDecimal>> priceMap = new HashMap<>();

        for (Pricing pricing : pg.getContent()) {
            List<PricingType> types = pricingTypeRepository.findByPricingId(pricing.getId());
            Map<SeatType, BigDecimal> m = new EnumMap<>(SeatType.class);
            for (PricingType pt : types) {
                m.put(pt.getType(), pt.getPrice());
            }
            priceMap.put(pricing.getId(), m);
        }

        model.addAttribute("page", pg);
        model.addAttribute("priceMap", priceMap);

        return "admin/pricing/pricing-list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        var form = new PricingForm();
        form.setName("");
        form.setAdultPrice(new BigDecimal("0.00"));
        form.setChildPrice(new BigDecimal("0.00"));

        model.addAttribute("pageTitle", "Create Pricing");
        model.addAttribute("form", form);
        model.addAttribute("mode", "create");

        return "admin/pricing/pricing-form";
    }

    @PostMapping
    public String createSubmit(@Valid @ModelAttribute("form") PricingForm form,
                               BindingResult binding,
                               RedirectAttributes ra,
                               Model model) {
        if (binding.hasErrors()) {
            System.out.println(binding);
            model.addAttribute("pageTitle", "Create Pricing");
            model.addAttribute("mode", "create");
            model.addAttribute("form", new PricingForm());

            return "admin/pricing/pricing-form";
        }

        var req = new PricingCreateRequest();
        req.name = form.getName();
        req.prices = new EnumMap<>(SeatType.class);
        req.prices.put(SeatType.ADULT, form.getAdultPrice());
        req.prices.put(SeatType.CHILD, form.getChildPrice());
        if ("ACTIVE".equalsIgnoreCase(form.getStatus())) {
            req.setStatus(Status.ACTIVE);
        } else {
            req.setStatus(Status.DEACTIVE);
        }
        try {
            Long id = service.create(req);
            ra.addFlashAttribute("success", "Pricing created.");
            return "redirect:/admin/pricing/" + id + "/edit";
        } catch (IllegalArgumentException ex) {
            binding.rejectValue("name", "name.exists", ex.getMessage());
            model.addAttribute("pageTitle", "Create Pricing");
            model.addAttribute("mode", "create");
            return "admin/pricing/pricing-form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var p = service.getOrThrow(id);
        var types = service.listTypes(id);
        Map<SeatType, BigDecimal> map = new EnumMap<>(SeatType.class);
        types.forEach(t -> map.put(t.getType(), t.getPrice()));

        var form = new PricingForm();
        form.setId(p.getId());
        form.setName(p.getName());
        form.setStatus(p.getStatus().name());
        form.setAdultPrice(map.getOrDefault(SeatType.ADULT, new BigDecimal("0.00")));
        form.setChildPrice(map.getOrDefault(SeatType.CHILD, new BigDecimal("0.00")));

        model.addAttribute("pageTitle", "Edit Pricing");
        model.addAttribute("form", form);
        model.addAttribute("mode", "edit");
        return "admin/pricing/pricing-form";
    }

    @PostMapping("/{id}")
    public String editSubmit(@PathVariable Long id,
                             @Valid @ModelAttribute("form") PricingForm form,
                             BindingResult binding,
                             RedirectAttributes ra,
                             Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Pricing");
            model.addAttribute("mode", "edit");
            return "admin/pricing/pricing-form";
        }
        var req = new PricingUpdateRequest();
        req.name = form.getName();
        req.prices = new EnumMap<>(SeatType.class);
        req.prices.put(SeatType.ADULT, form.getAdultPrice());
        req.prices.put(SeatType.CHILD, form.getChildPrice());
        if ("ACTIVE".equalsIgnoreCase(form.getStatus())) {
            req.setStatus(Status.ACTIVE);
        } else {
            req.setStatus(Status.DEACTIVE);
        }
        try {
            service.update(id, req);
            ra.addFlashAttribute("success", "Pricing updated.");
            return "redirect:/admin/pricing";
        } catch (IllegalArgumentException ex) {
            binding.rejectValue("name", "name.exists", ex.getMessage());
            model.addAttribute("pageTitle", "Edit Pricing");
            model.addAttribute("mode", "edit");
            return "admin/pricing/pricing-form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("info", "Pricing deleted.");
        return "redirect:/admin/pricing";
    }
}