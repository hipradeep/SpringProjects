package com.hipradeep.code.controller;

import com.hipradeep.code.config.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public Map<String, String> login(
            @RequestBody LoginRequest req) {

        Authentication authentication =
                authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        req.getUsername(),
                        req.getPassword()
                    )
                );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("status", "LOGIN SUCCESS");
        return response;
    }

    @GetMapping("/welcome")
    public String welcome(Authentication authentication) {
        // Extract the username statelessly from the JWT authentication context
        return "Welcome, " + authentication.getName() + "!";
    }
}
