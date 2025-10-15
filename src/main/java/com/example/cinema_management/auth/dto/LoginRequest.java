package com.example.cinema_management.auth.dto;

import jakarta.validation.constraints.*;

public class LoginRequest {
    @NotBlank @Email @Size(max = 100)
    private String email;

    @NotBlank @Size(min = 6, max = 72)
    private String password;

    // getters/setters ...
}
