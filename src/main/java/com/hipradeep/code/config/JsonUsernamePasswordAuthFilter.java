package com.hipradeep.code.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hipradeep.code.dto.AuthRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JsonUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JsonUsernamePasswordAuthFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/login");  // login URL
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            AuthRequest loginRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), AuthRequest.class);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    );

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("Invalid login request format");
        }
    }

}
