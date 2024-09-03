package com.example.bucket;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenBucketRateLimiterImplTest {



        private static final String BOB = "Bob";

        @Test
        void allowed_burstyTraffic_acceptsAllAccumulatedRequestsWithinRateLimitThresholds() {
            Clock clock = mock(Clock.class);
            when(clock.millis()).thenReturn(0L, 0L, 1L, 4001L, 4002L, 4003L, 4004L, 4005L);

            TokenBucketRateLimiterImpl limiter
                    = new TokenBucketRateLimiterImpl(4, Duration.ofSeconds(1), 1, clock);
            // 0 seconds passed
            assertTrue(limiter.allowedRequest(BOB),
                    "Bob's request 1 at timestamp=0 must pass," +
                            " because bucket has 1 token available");
            assertFalse(limiter.allowedRequest(BOB),
                    "Bob's request 2 at timestamp=1 must not be allowed," +
                            " because bucket has 0 tokens available");

            // 4 seconds passed
            assertTrue(limiter.allowedRequest(BOB),
                    "Bob's request 3 at timestamp=4001 must pass," +
                            " because bucket accumulated 4 tokens" +
                            " since the last refill at timestamp=0" +
                            " and now has 4 tokens available");
            assertTrue(limiter.allowedRequest(BOB),
                    "Bob's request 4 at timestamp=4002 must pass," +
                            " because bucket has 3 tokens available");
            assertTrue(limiter.allowedRequest(BOB),
                    "Bob's request 5 at timestamp=4003 must pass," +
                            " because bucket has 2 tokens available");
            assertTrue(limiter.allowedRequest(BOB),
                    "Bob's request 6 at timestamp=4004 must pass," +
                            " because bucket has 1 token available");
            assertFalse(limiter.allowedRequest(BOB),
                    "Bob's request 7 at timestamp=4005 must not be allowed," +
                            " because bucket has 0 tokens available");
        }

    }
