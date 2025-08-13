package com.hipradeep.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
        //Change Token Parameter Name
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setParameterName("custom_csrf"); // default is "_csrf"

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/home")
                        ).permitAll()

                        .requestMatchers("/dashboard").hasRole("USER")  // Only ADMIN can access
                        //.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // USER or ADMIN can access


                        .anyRequest().authenticated()  // All other requests require login
                )

                .formLogin(form -> form
                        .loginPage("/login")  // Custom login page
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                // ============== CSRF (DEFAULT: HttpSession) ===============
                .csrf(csrf -> csrf
                        // No need to configure repository - uses HttpSession by default
                        // Optional: Customize CSRF token handling (for AJAX/SPA compatibility)
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .csrfTokenRepository(repository)
                )
                // ============== CSRF (COOKIE STORAGE) ========================
//                .csrf(csrf -> csrf
//                            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Store in cookie
//                            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()) // For modern SPAs
//                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
        return http.build();
    }


    //Customise this in CustomUserDetailsService
    //@Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        //return NoOpPasswordEncoder.getInstance(); // Plain text passwords
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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
