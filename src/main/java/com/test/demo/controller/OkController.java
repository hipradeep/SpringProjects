package com.test.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OkController {

    @GetMapping("/ok")
    public String showOkPage(Model model) {
        model.addAttribute("message", "Everything is working fine!");
        return "ok"; // This refers to ok.html in src/main/resources/templates
    }
}
