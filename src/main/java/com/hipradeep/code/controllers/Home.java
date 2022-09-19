package com.hipradeep.code.controllers;

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
    @RequestMapping("/welcome")
    public String homeAS()
    {
        return "Welcome Pradeep";
    }
}
