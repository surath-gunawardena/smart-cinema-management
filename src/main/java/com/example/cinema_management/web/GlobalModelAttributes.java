package com.example.cinema_management.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    @ModelAttribute("path")
    public String path(HttpServletRequest request) {
        return request != null && request.getRequestURI() != null ? request.getRequestURI() : "";
    }
}
