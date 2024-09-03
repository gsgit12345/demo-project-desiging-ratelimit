package com.example.exceptionn;

public class RateLimitExceededException    extends RuntimeException {
    public RateLimitExceededException(final String message) {
        super(message);
    }
}
