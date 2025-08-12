package com.hipradeep.code.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        System.out.println("Hi, Im from Controller");
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    @GetMapping("/dashboard-user")
    public String dashboardUser() {
        return "dashboard-user";
    }
}