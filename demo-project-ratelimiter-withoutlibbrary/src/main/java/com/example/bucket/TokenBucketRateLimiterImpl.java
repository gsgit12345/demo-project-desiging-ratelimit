package com.example.bucket;

import java.time.Clock;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class TokenBucketRateLimiterImpl {

    private final int capacity;
    private final Duration period;
    private final int tokensPerPeriod;
    private final Clock clock;
    private final Map<String, TokenBucket> userTokenBucket = new HashMap<>();

    public TokenBucketRateLimiterImpl(int capacity, Duration period,
                                      int tokensPerPeriod, Clock clock) {
        this.capacity = capacity;
        this.period = period;
        this.tokensPerPeriod = tokensPerPeriod;
        this.clock = clock;
    }

    public boolean allowedRequest(String userId) {
        // Initialize an empty bucket for new users or retrieve existing one.
        TokenBucket tokenBucket = userTokenBucket.computeIfAbsent(userId,
                k -> new TokenBucket(clock.millis(), tokensPerPeriod));

        // Refill the bucket with available tokens based on
        // elapsed time since last refill.
        tokenBucket.refill();

        // Allow this request if a token was available and consumed,
        // Otherwise, reject the request.
        return tokenBucket.consume();
    }

    private class TokenBucket {
        private long refillTimestamp; // Timestamp of the last refill.
        private long tokenCount; // Current number of tokens in the bucket.

        TokenBucket(long refillTimestamp, long tokenCount) {
            this.refillTimestamp = refillTimestamp;
            this.tokenCount = tokenCount;
        }

        /**
         * Regenerates tokens at fixed intervals. Waits for the entire period
         * to elapse before regenerating the full amount of tokens
         * designated for that period.
         */
        private void refill() {
            long now = clock.millis();
            long elapsedTime = now - refillTimestamp;
            long elapsedPeriods = elapsedTime / period.toMillis();
            long availableTokens = elapsedPeriods * tokensPerPeriod;

            tokenCount = Math.min(tokenCount + availableTokens, capacity);
            refillTimestamp += elapsedPeriods * period.toMillis();
        }

        boolean consume() {
            if (tokenCount > 0) {
                --tokenCount;
                return true;
            } else {
                return false;
            }
        }
    }
}