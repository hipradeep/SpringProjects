package com.hipradeep.code.config;

import com.hipradeep.code.entity.UserEntity;
import com.hipradeep.code.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtFilter jwtFilter;

    // 1. In-Memory UserDetailsService (UDS1)
    @Bean
    @ConditionalOnProperty(name = "security.auth.type", havingValue = "inmemory", matchIfMissing = true)
    public UserDetailsService inMemoryUserDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user1 = User.builder()
                .username("john")
                .password(passwordEncoder.encode("password123"))
                .roles("USER")
                .build();

        UserDetails user2 = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN", "USER")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }

    // 2. Database Authentication (UDS2)
    @Bean
    @ConditionalOnProperty(name = "security.auth.type", havingValue = "jdbc")
    public UserDetailsService userDetailsService() {
        return username -> {
            UserEntity user = repo.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();
        };
    }

    @Bean
    @ConditionalOnProperty(name = "security.auth.type", havingValue = "jdbc")
    public UserDetailsService customDetailsService() {

        return username -> {
            UserEntity user = repo.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
            return new CustomUserDetails(user);
        };
    }

    // 4. Default AuthenticationProvider (UDS-based DaoAuthenticationProvider)
    @Bean
    @ConditionalOnProperty(name = "security.provider.type", havingValue = "default", matchIfMissing = true)
    public AuthenticationProvider defaultAuthenticationProvider(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // 6. Default AuthenticationManager (Standard Spring ProviderManager)
    @Bean
    @ConditionalOnProperty(name = "security.manager.type", havingValue = "default", matchIfMissing = true)
    public AuthenticationManager defaultAuthenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 8. BCrypt PasswordEncoder (BCrypt Strong Hashing)
    @Bean
    @ConditionalOnProperty(name = "security.encoder.type", havingValue = "bcrypt", matchIfMissing = true)
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login")
                        .permitAll()
                        .anyRequest()
                        .authenticated())

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Send 401 Unauthorized instead of standard redirect
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        }))

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
