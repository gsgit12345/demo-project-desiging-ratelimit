package com.example.ratelimiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RateLimiter {

    private final Map<String, RateLimiterStateAPI> registeredAPIs;
    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimiter.class);

    RateLimiter() {
        registeredAPIs = new HashMap<>();
    }

    void configure(RateLimiterConfig rateLimiterConfig) {
        // TO DO : do validation
        registeredAPIs.put(rateLimiterConfig.name(), new RateLimiterStateAPI(rateLimiterConfig));
    }

    synchronized boolean isRateLimited(String name) {

        if (registeredAPIs.containsKey(name)) {
            RateLimiterStateAPI apiRateLimiterState = registeredAPIs.get(name);
            long currentTime = System.currentTimeMillis();
            long timeElapsedSinceBucketStart = currentTime - apiRateLimiterState.getStartTime();
            LOGGER.debug("Available Tokens = {}, Elapsed = {} ms", apiRateLimiterState.getAvailableTokens(), timeElapsedSinceBucketStart);
            if (apiRateLimiterState.getStartTime() == 0L) {
                LOGGER.debug("First API call. Setting the start time to current time, decreasing the available token count by 1 and allowing the request");
                apiRateLimiterState.setStartTime(currentTime);
                apiRateLimiterState.setAvailableTokens(apiRateLimiterState.getAvailableTokens() - 1);
                return false;
            } else if (timeElapsedSinceBucketStart >= apiRateLimiterState.rateLimiterConfig.limitPeriodInSeconds() * 1000L) {
                LOGGER.debug("Rate Limiting Period Time Elapsed. Resetting the start time to current time and token count to original token count -1 and allowing the request");
                apiRateLimiterState.setStartTime(currentTime);
                apiRateLimiterState.setAvailableTokens(apiRateLimiterState.getRateLimiterConfig().limitForPeriod() - 1);
                return false;
            } else if (timeElapsedSinceBucketStart < apiRateLimiterState.rateLimiterConfig.limitPeriodInSeconds() * 1000L
                    && apiRateLimiterState.getAvailableTokens() >= 1) {
                LOGGER.debug("Rate limit not reached, decreasing the available token counts by 1 and allowing the request");
                apiRateLimiterState.setAvailableTokens(apiRateLimiterState.getAvailableTokens() - 1);
                return false;
            } else {
                LOGGER.debug("Rate Limit threshold reached");
                return true;
            }
        } else {
            LOGGER.debug("Rate limit not configured for the endpoint {}" , name);
            return false;
        }
    }
}