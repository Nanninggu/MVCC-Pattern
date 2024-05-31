package com.example.MultiVersion.Concurrency.Control_Pattern.service;

import org.springframework.stereotype.Service;

@Service
public class ConcurrencyService {
    private int counter = 0;

    public synchronized int incrementCounter() {
        return ++counter;
    }
}
