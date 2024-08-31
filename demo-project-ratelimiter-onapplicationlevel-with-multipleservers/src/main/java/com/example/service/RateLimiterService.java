package com.example.service;

import com.example.dto.RateLimiterRequest;
import com.example.dto.RateLimiterResponse;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
    public RateLimiterResponse process(RateLimiterRequest request)
    {
        return new RateLimiterResponse();
    }
}
