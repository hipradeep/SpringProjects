package com.hipradeep.code.config;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
//@EnableWebSecurity
public class SecurityConfig2 {

    private final UserDetailsService userDetailsService;

    public SecurityConfig2(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider() {
            @Override
            protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                          UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
                String presentedPassword = authentication.getCredentials().toString();
                String storedPassword = userDetails.getPassword();

                System.out.println("=== PASSWORD COMPARISON ===");
                System.out.println("Presented: " + presentedPassword);
                System.out.println("Stored: " + storedPassword);
                System.out.println("Encoder: " + getPasswordEncoder().getClass().getSimpleName());

                boolean matches = getPasswordEncoder().matches(presentedPassword, storedPassword);
                System.out.println("Match result: " + matches);

                System.out.println("User roles:");
                userDetails.getAuthorities().forEach(auth -> System.out.println(" - " + auth.getAuthority()));

                System.out.println("=========================");

                if (!matches) {
                    throw new BadCredentialsException("Bad credentials");
                }
            }
        };

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Expose AuthenticationManager from AuthenticationConfiguration
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        CustomUsernamePasswordAuthenticationFilter customFilter = new CustomUsernamePasswordAuthenticationFilter();
        customFilter.setAuthenticationManager(authManager);
        customFilter.setFilterProcessesUrl("/loginpost"); //make sure login submitted by post on loginpost, in login form

        customFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            boolean isUser = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                response.sendRedirect("/dashboard");
            } else if (isUser) {
                response.sendRedirect("/dashboard-user");
            } else {
                response.sendRedirect("/accessDenied");
            }
        });

        customFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            response.sendRedirect("/login?error");
        });

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .addFilterAt(customFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

