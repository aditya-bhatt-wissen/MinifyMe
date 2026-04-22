package com.app.MinifyMe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class URLController {

    @GetMapping
    public String home() {
        return "Welcome to MinifyMe!";
    }

}