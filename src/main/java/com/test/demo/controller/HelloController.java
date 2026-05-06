package com.test.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("/hello")
    // @ResponseBody
    public String sayHello() {
        return "Hello from Spring Boot 3 with @ResponseBody!";
    }
}
