package com.example.Finguard.domain.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret:MySecretKeyForJWTTokenGenerationThatIsAtLeast256Bits}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}") // 24 hours in milliseconds
    private long expirationTime;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(UUID userId, String username, String role) {
        logger.info("Generating token for user: {} (ID: {})", username, userId);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        String token = Jwts.builder()
                .setSubject(userId.toString())
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();

        logger.info("Token generated: {}...", token.substring(0, Math.min(20, token.length())));
        return token;
    }

    public UUID getUserIdFromToken(String token) {
        logger.debug("Extracting user ID from token");
        Claims claims = getAllClaimsFromToken(token);
        UUID userId = UUID.fromString(claims.getSubject());
        logger.debug("User ID extracted: {}", userId);
        return userId;
    }

    public String getUsernameFromToken(String token) {
        logger.debug("Extracting username from token");
        Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("username");
    }

    public String getRoleFromToken(String token) {
        logger.debug("Extracting role from token");
        Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("role");
    }

    public boolean validateToken(String token) {
        try {
            logger.debug("Validating token");
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .parseClaimsJws(token);
            logger.debug("Token is valid");
            return true;
        } catch (Exception e) {
            logger.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
