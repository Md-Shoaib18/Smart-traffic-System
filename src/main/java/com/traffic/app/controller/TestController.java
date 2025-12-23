package com.traffic.app.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Spring Boot is working fine!";
    }

    @GetMapping("/test")
    public String test() {
        return "Mic Testing!";
    }
}
