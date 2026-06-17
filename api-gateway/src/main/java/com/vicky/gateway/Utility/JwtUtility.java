package com.vicky.gateway.Utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtility {

    @Value("${jwt.secret.key}")
    private String SECRET;

    private SecretKey SECRET_KEY;

    @PostConstruct
    public void init() {
        this.SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    }


    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isExpired(String token) {
        Claims claim = getClaims(token);
        return claim.getExpiration().before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            return !isExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public String extractRoles(String token) {
        Claims claims = getClaims(token);
        List<?> roles = claims.get("roles", List.class);
        if (roles == null || roles.isEmpty()) {
            return "";
        }
        return String.join(",", roles.stream()
                .map(obj -> obj.toString())
                .toList());
    }

    public String extractEmail(String token) {
        Claims claims = getClaims(token);
        String email = claims.get("email", String.class);
        if (email == null || email.isEmpty()) {
            return "exampleEmail@email.com";
        }
        return email;
    }
}
