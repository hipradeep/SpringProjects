package com.hipradeep.code.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
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
