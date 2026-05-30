package com.hipradeep.code.config;

import com.hipradeep.code.entity.UserEntity;
import com.hipradeep.code.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.hipradeep.code.entity.RoleEntity;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public UserDetailsService userDetailsService() {

        return username -> {

            UserEntity user = repo.findByUsername(username)
                    .orElseThrow(() ->
                        new UsernameNotFoundException(
                            "User Not Found"
                        )
                    );

            List<GrantedAuthority> authorities;
            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(r -> "ROLE_ADMIN".equalsIgnoreCase(r.getName()));

            if (isAdmin) {
                authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN"),
                        new SimpleGrantedAuthority("ROLE_USER"),
                        new SimpleGrantedAuthority("OP_READ"),
                        new SimpleGrantedAuthority("OP_WRITE"),
                        new SimpleGrantedAuthority("OP_DELETE")
                );
            } else {
                authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_USER"),
                        new SimpleGrantedAuthority("OP_READ")
                );
            }

            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(authorities)
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                userDetailsService()
        );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/login", "/api/public/**").permitAll()
                    .requestMatchers("/api/admin/**").hasRole("ADMIN") // Role-Based Path Security
                    .requestMatchers("/api/delete/**").hasAuthority("OP_DELETE") // Permission-Based Path Security
                    .requestMatchers("/api/private/**").authenticated()
                    .requestMatchers("/api/restricted/**").denyAll()
                    .anyRequest().authenticated()
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
