package com.hipradeep.code.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authManager;

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {
            Authentication authRequest =
                    new UsernamePasswordAuthenticationToken(username, password);

            Authentication authentication = authManager.authenticate(authRequest);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

            return "redirect:/welcome";
        } catch (AuthenticationException e) {
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/welcome")
    @ResponseBody
    public String welcome(Authentication authentication) {
        if (authentication == null) {
            return "Welcome, Anonymous!";
        }
        return "Welcome, " + authentication.getName() + "!";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/welcome";
    }
}
