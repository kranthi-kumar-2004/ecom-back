package com.student.management.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "mysecretkeymysecretkeymysecretkey123456";
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    private static final long EXPIRATION_TIME = 86400000; // 1 day

    public static String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Long extractUserId(String token) {

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Authorization header is missing ❌");
        }

        token = cleanToken(token);

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return Long.parseLong(claims.getSubject());

        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expired ❌");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid token ❌");
        }  catch (Exception e) {
            throw new RuntimeException("Token parsing failed ❌");
        }
    }

    public static boolean validateToken(String token) {
        try {
            token = cleanToken(token);

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (Exception e) {
            System.out.println("Invalid token ❌");
            return false;
        }
    }

    private static String cleanToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}