package com.hipradeep.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @PostMapping("/login")
    public String login(
            @RequestBody LoginRequest req) {

        Authentication authentication =
                authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        req.getUsername(),
                        req.getPassword()
                    )
                );

        return "LOGIN SUCCESS";
    }
}
