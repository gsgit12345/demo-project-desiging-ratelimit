package com.example.ratelimiter;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RateLimiterTest {

    RateLimiter rateLimiter = new RateLimiter();
    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimiterTest.class);
    static class RateLimiterConfigImpl implements RateLimiterConfig {

        String name;
        int limit;
        int durationInSeconds;

        RateLimiterConfigImpl(String name, int maxRequest, int durationInSeconds) {
            this.name = name;
            this.limit = maxRequest;
            this.durationInSeconds = durationInSeconds;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public int limitForPeriod() {
            return limit;
        }

        @Override
        public int limitPeriodInSeconds() {
            return durationInSeconds;
        }

    }

    @Test
    public void isRateLimited() {
        RateLimiterConfigImpl doSomethingRateLimiter = new RateLimiterConfigImpl("doSomething", 2, 1);
        rateLimiter.configure(doSomethingRateLimiter);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 200000; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    doSomething(finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        LOGGER.debug("Finished all threads");
    }

    private void doSomething(int i) throws InterruptedException {
        if (!rateLimiter.isRateLimited("doSomething")) {
            Thread.sleep(1000);
            LOGGER.debug("Processing {}",i);
        } else {
            LOGGER.debug("{} This is rate limited",i);
        }
    }

}
