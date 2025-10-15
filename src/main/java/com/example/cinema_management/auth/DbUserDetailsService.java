package com.example.cinema_management.auth;

import com.example.cinema_management.user.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbUserDetailsService implements UserDetailsService {

    private final UserRepository users;

    public DbUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAccount u = users.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Map Role -> ROLE_*
        String authority = switch (u.getRole()) {
            case ADMIN -> "ROLE_ADMIN";
            case GATE_KEEPER -> "ROLE_GATE_KEEPER";
            default -> "ROLE_REGISTERED_USER";
        };
        return new org.springframework.security.core.userdetails.User(
                u.getEmail(), u.getPassword(), List.of(new SimpleGrantedAuthority(authority))
        );
    }
}
