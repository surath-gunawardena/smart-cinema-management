package com.example.cinema_management.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminUiController {

    @GetMapping({"/admin", "/admin/dashboard"})
    public String dashboard(Model model) {
        // Demo metrics – replace with real service calls
        model.addAttribute("activeMovies", 4);
        model.addAttribute("totalUsers", 1);
        model.addAttribute("schedules", 3);
        model.addAttribute("totalRevenue", 139.92);
        model.addAttribute("adminName", "Admin User");
        return "admin/dashboard";
    }
}
