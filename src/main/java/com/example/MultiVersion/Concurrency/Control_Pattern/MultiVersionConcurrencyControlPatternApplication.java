package com.example.MultiVersion.Concurrency.Control_Pattern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class MultiVersionConcurrencyControlPatternApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiVersionConcurrencyControlPatternApplication.class, args);
    }
}
