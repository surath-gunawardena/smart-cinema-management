package com.example.cinema_management.audit;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_audit")
public class LoginAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private Boolean success;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime loginAt;

    // --- Manual Builder pattern ---
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final LoginAudit audit = new LoginAudit();

        public Builder username(String username) {
            audit.username = username;
            return this;
        }

        public Builder success(Boolean success) {
            audit.success = success;
            return this;
        }

        public Builder ipAddress(String ipAddress) {
            audit.ipAddress = ipAddress;
            return this;
        }

        public Builder userAgent(String userAgent) {
            audit.userAgent = userAgent;
            return this;
        }

        public Builder loginAt(LocalDateTime loginAt) {
            audit.loginAt = loginAt;
            return this;
        }

        public LoginAudit build() {
            return audit;
        }
    }
}
