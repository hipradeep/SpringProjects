package com.test.demo.controller;

import com.test.demo.annotation.Log;
import com.test.demo.annotation.Print;
import com.test.demo.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@Log(level = "INFO")
public class TestController {

    @GetMapping("/test")
    @Log(level = "INFO", includeArgs = true, includeResult = true)
    public String test(@RequestParam(defaultValue = "Guest") String name) {
        return "Hello, " + name + "!";
    }

    @GetMapping("/debug")
    @Log(level = "DEBUG", includeArgs = true)
    public String debug() {
        return "Debugging...";
    }

    @GetMapping("/warn")
    @Log(level = "WARN")
    public String warn() {
        return "Warning log test";
    }

    @GetMapping("/print")
    @Print("SysOut: Print Annotation Triggered!")
    public String print() {
        return "Check console for System.out.println output";
    }

    @PostMapping("/user")
    public String createUser(@Valid @RequestBody UserDto userDto) {
        return "User created successfully with PAN: " + userDto.getPanCard();
    }
}
