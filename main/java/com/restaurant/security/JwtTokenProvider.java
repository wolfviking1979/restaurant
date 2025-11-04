package com.restaurant.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpirationInMs;

    private SecretKey getSigningKey() {
        // Для HS512 ключ должен быть не менее 64 байт
        byte[] keyBytes = jwtSecret.getBytes();
        if (keyBytes.length < 64) {
            // Дополняем ключ до нужной длины
            byte[] paddedKey = new byte[64];
            System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 64));
            return Keys.hmacShaKeyFor(paddedKey);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SecurityException ex) {
            // Invalid JWT signature
            System.err.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            // Invalid JWT token
            System.err.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            // Expired JWT token
            System.err.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            // Unsupported JWT token
            System.err.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            // JWT claims string is empty
            System.err.println("JWT claims string is empty");
        } catch (JwtException ex) {
            // General JWT exception
            System.err.println("JWT exception: " + ex.getMessage());
        }
        return false;
    }

    public long getExpirationInMs() {
        return jwtExpirationInMs;
    }
}