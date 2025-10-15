package com.example.cinema_management.service;

import com.example.cinema_management.audit.LoginAudit;
import com.example.cinema_management.audit.LoginAuditRepository;
import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class LoginAuditSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final LoginAuditRepository repository;

    public LoginAuditSuccessHandler(LoginAuditRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) throws IOException {
        repository.save(LoginAudit.builder()
                .username(auth.getName())
                .ipAddress(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .success(true)
                .loginAt(LocalDateTime.now())
                .build());

        // Decide redirect by role
        boolean isAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN"));
        boolean isGate = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_GATE_KEEPER"));

        String target = isAdmin ? "/admin/dashboard" : (isGate ? "/gatekeeper" : "/");
        getRedirectStrategy().sendRedirect(request, response, target);
    }
}
