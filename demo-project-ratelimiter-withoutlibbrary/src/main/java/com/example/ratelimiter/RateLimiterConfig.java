package com.example.ratelimiter;

public interface RateLimiterConfig {

    int limitForPeriod();

    int limitPeriodInSeconds();

    String name();
}
