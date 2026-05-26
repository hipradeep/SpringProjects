package com.hipradeep.code.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping({"/", "/welcome"})
    public String welcome(Authentication authentication) {
        // Extract the username statefully from the Spring Security session context
        return "Welcome, " + authentication.getName() + "!";
    }
}
