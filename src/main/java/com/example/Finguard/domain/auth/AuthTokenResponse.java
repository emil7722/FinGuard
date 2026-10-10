package com.example.Finguard.domain.auth;

import java.util.UUID;

public class AuthTokenResponse {
    private String token;
    private UUID userId;
    private String username;
    private String role;

    public AuthTokenResponse(String token, UUID userId, String username, String role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    // Getters
    public String getToken() { return token; }
    public UUID getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
}
