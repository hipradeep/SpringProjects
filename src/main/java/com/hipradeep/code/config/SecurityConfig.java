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

    // 3. Custom UserDetailsService (CustomUDS3)
    @Bean
    @ConditionalOnProperty(name = "security.auth.type", havingValue = "custom")
    public UserDetailsService customUserDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    // 4. Default AuthenticationProvider (UDS-based DaoAuthenticationProvider)
    @Bean
    @ConditionalOnProperty(name = "security.provider.type", havingValue = "default", matchIfMissing = true)
    public AuthenticationProvider defaultAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // 5. Custom AuthenticationProvider (Custom Credential Validation)
    @Bean
    @ConditionalOnProperty(name = "security.provider.type", havingValue = "custom")
    public AuthenticationProvider customAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    // 6. Default AuthenticationManager (Standard Spring ProviderManager)
    @Bean
    @ConditionalOnProperty(name = "security.manager.type", havingValue = "default", matchIfMissing = true)
    public AuthenticationManager defaultAuthenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 7. Custom AuthenticationManager (Delegator to active AuthenticationProvider)
    @Bean
    @ConditionalOnProperty(name = "security.manager.type", havingValue = "custom")
    public AuthenticationManager customAuthenticationManager(AuthenticationProvider authenticationProvider) {
        return new CustomAuthenticationManager(authenticationProvider);
    }


    // 8. BCrypt PasswordEncoder (BCrypt Strong Hashing)
    @Bean
    @ConditionalOnProperty(name = "security.encoder.type", havingValue = "bcrypt", matchIfMissing = true)
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 9. SHA-2 PasswordEncoder (SHA-256 Cryptographic Hash)
    @Bean
    @ConditionalOnProperty(name = "security.encoder.type", havingValue = "sha2")
    public PasswordEncoder sha2PasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
                    StringBuilder hexString = new StringBuilder();
                    for (byte b : hash) {
                        String hex = Integer.toHexString(0xff & b);
                        if (hex.length() == 1) hexString.append('0');
                        hexString.append(hex);
                    }
                    return hexString.toString();
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("SHA-256 algorithm not found", e);
                }
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encode(rawPassword).equalsIgnoreCase(encodedPassword);
            }
        };
    }

    // 10. NoOp PasswordEncoder (Plaintext Matcher)
    @Bean
    @ConditionalOnProperty(name = "security.encoder.type", havingValue = "noop")
    public PasswordEncoder noopPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
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
                    .authenticated()
            )

            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) -> {
                        // Send 401 Unauthorized instead of standard redirect
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    })
            )

            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
