package com.example.MultiVersion.Concurrency.Control_Pattern.controller;

import com.example.MultiVersion.Concurrency.Control_Pattern.service.ConcurrencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concurrency")
public class ConcurrencyController {
    private final ConcurrencyService concurrencyService;

    public ConcurrencyController(ConcurrencyService concurrencyService) {
        this.concurrencyService = concurrencyService;
    }

    @GetMapping("/increment")
    public int incrementCounter() {
        return concurrencyService.incrementCounter();
    }
}