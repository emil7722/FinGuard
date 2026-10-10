package com.example.Finguard.domain.auth;

import java.util.Objects;
import java.util.UUID;

public class UserPrincipal {
    private final UUID userId;
    private final String username;
    private final String role;

    public UserPrincipal(UUID userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPrincipal that)) return false;
        return Objects.equals(userId, that.userId)
                && Objects.equals(username, that.username)
                && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, role);
    }
}
