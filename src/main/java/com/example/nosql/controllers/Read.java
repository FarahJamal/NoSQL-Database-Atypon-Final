package com.example.nosql.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class Read {
    @GetMapping("/test-connection")
    public Object testConnection() {
        return System.getProperties();
    }
}
