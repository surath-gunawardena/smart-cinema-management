package com.example.cinema_management.config;

import com.example.cinema_management.service.LoginAuditSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfig {

    private final LoginAuditSuccessHandler loginAuditSuccessHandler;
    private final UserDetailsService dbUserDetailsService;

    public SecurityConfig(LoginAuditSuccessHandler loginAuditSuccessHandler,
                          UserDetailsService dbUserDetailsService) {
        this.loginAuditSuccessHandler = loginAuditSuccessHandler;
        this.dbUserDetailsService = dbUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(dbUserDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/public/**", "/css/**", "/login", "/images/**",
                                "/auth/register").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/gatekeeper/**").hasRole("GATE_KEEPER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/admin/dashboard", true) // fallback
                        .failureUrl("/login?error=true")
                        .successHandler(loginAuditSuccessHandler)     // still logs & redirects by role
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/perform_logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
