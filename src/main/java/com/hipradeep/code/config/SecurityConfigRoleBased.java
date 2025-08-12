package com.hipradeep.code.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigRoleBased {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        String[] permitAllUrls = {"/authenticate", "/public/**"};
        String[] authenticatedUrls = {"/welcome", "/profile/**"};
        String[] userRoleUrls = {"/home/premium", "/user/**"};
        String[] adminRoleUrls = {"/home/trunk", "/admin/**"};


        http
                .csrf(csrf -> csrf.disable()) // Typically disabled for APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permitAllUrls).permitAll()             // All these URLs are allowed without auth
                        .requestMatchers(authenticatedUrls).authenticated()     // JWT required for these URLs
                        .requestMatchers(userRoleUrls).hasRole("USER")          // USER role required for these URLs
                        .requestMatchers(adminRoleUrls).hasRole("ADMIN")          // ADMIN role required for these URLs
                        .anyRequest().denyAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Send 401 Unauthorized instead of redirect
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // No sessions

        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}

