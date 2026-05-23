package com.hipradeep.code.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        String[] permitAllUrls = {"/public/**"};
        String[] authenticatedUrls = {"/welcome", "/profile/**"};
        String[] userRoleUrls = {"/home/premium", "/user/**"};
        String[] adminRoleUrls = {"/home/trunk", "/admin/**"};

        http
                .csrf(csrf -> csrf.disable()) // Typically disabled for APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permitAllUrls).permitAll()             // All these URLs are allowed without auth
                        .requestMatchers(authenticatedUrls).authenticated()     // Auth required for these URLs
                        .requestMatchers(userRoleUrls).hasRole("USER")          // USER role required for these URLs
                        .requestMatchers(adminRoleUrls).hasRole("ADMIN")        // ADMIN role required for these URLs
                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults()) // Enable HTTP Basic Authentication
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Send 401 Unauthorized instead of redirect
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // No sessions

        return http.build();
    }
}
