package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // Route simple pour afficher "Hello world!"
    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello world!";
    }
}
