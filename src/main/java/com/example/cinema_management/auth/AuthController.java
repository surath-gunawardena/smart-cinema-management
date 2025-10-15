package com.example.cinema_management.auth;

import com.example.cinema_management.auth.dto.RegisterRequest;
import com.example.cinema_management.user.UserAccount;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {
        UserAccount saved = auth.register(req);
        return ResponseEntity.ok().body(
                java.util.Map.of("id", saved.getUser_id(), "email", saved.getEmail(), "role", saved.getRole().name())
        );
    }
}
