package com.example.bucket;

public interface RateLimiter {
    boolean tryAcquire();
}

