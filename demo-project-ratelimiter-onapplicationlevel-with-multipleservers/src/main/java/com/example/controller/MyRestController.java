package com.example.controller;

import com.example.RateLimiterWithMultipleServer;
import com.example.dto.RateLimiterRequest;
import com.example.dto.RateLimiterResponse;
import com.example.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MyRestController{
        @Autowired
        RateLimiterService myService;

        @PostMapping
        public RateLimiterResponse processRequest(@RequestBody final RateLimiterRequest request) {
            return myService.process(request);
        }
}
