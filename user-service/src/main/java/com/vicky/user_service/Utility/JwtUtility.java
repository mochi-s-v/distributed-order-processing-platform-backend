package com.vicky.user_service.Utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtility {

    private final String SECRET = "my-super-secret-key-that-is-long-enough-1234567890!@#$";

    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username, String role) {
        final long accessExpiration = 3600000;
        return Jwts.builder()
                .claim("roles", List.of(role))
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(String username) {
        final long refreshExpiration = 604800000;
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(SECRET_KEY)
                .compact();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

//    public boolean validateToken(String username, UserDetails userDetails, String token) {
//        return userDetails.getUsername().equals(username) && !isExpired(token);
//    }

//    public String extractUsername(String token) {
//        return getClaims(token).getSubject();
//    }
//
//    public boolean isExpired(String token) {
//        Claims claim = getClaims(token);
//        return claim.getExpiration().before(new Date());
//    }
}
