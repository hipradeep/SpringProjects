package com.hipradeep.code.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

    //http://localhost:8080/
    @RequestMapping("/")
    public String helloGFG()
    {
        return "Hello Pradeep";
    }

    //http://localhost:8080/welcome
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome, authenticated user!";
    }



    @GetMapping("/home/premium")
    public String premium() {
        return "Welcome to the premium section, USER role!";
    }

    @GetMapping("/home/trunk") //access by ADMIN/USER
    public String trunk() {
        return "Welcome to the premium section, ADMIN/USER role!";
    }
}
