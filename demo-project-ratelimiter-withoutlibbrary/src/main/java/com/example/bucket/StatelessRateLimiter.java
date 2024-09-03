package com.example.bucket;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;

import java.util.Date;

public class StatelessRateLimiter {
    private static final String SECRET_KEY = "mySecretKey";
    private static final int MAX_REQUESTS = 10;
    private static final int ONE_HOUR = 3600000;
    public String generateToken() {
        long expirationTime = System.currentTimeMillis() + ONE_HOUR;
        return Jwts.builder()
                .setSubject("rateLimitToken")
                .claim("requests", 0)
                .setExpiration(new Date(expirationTime))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    public boolean allowRequest(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            int requests = claims.get("requests", Integer.class);
            if (requests < MAX_REQUESTS) {
                claims.put("requests", requests + 1);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
