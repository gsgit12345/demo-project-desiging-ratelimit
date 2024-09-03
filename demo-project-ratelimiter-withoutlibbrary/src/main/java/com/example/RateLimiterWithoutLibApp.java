package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@SpringBootApplication
public class RateLimiterWithoutLibApp {
    public static void main(String[] args) {
        SpringApplication.run(RateLimiterWithoutLibApp.class);
        System.out.println("Hello World!");
    }
}
