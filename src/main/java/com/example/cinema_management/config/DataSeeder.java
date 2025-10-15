package com.example.cinema_management.config;

import com.example.cinema_management.user.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedUsers(UserRepository repo, PasswordEncoder enc) {
        return args -> {
            if (!repo.existsByEmail("admin@cinema.com")) {
                UserAccount admin = new UserAccount();
                admin.setName("Admin");
                admin.setEmail("admin@cinema.com");
                admin.setPassword(enc.encode("admin123"));
                admin.setRole(Role.ADMIN);
                repo.save(admin);
            }
            if (!repo.existsByEmail("gate@cinema.com")) {
                UserAccount gate = new UserAccount();
                gate.setName("Gate Keeper");
                gate.setEmail("gate@cinema.com");
                gate.setPassword(enc.encode("gate123"));
                gate.setRole(Role.GATE_KEEPER);
                repo.save(gate);
            }
        };
    }
}
