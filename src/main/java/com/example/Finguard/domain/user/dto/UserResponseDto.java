package com.example.Finguard.domain.user.dto;

import java.util.UUID;

public class UserResponseDto {
    private UUID id;
    private String fullName;
    private String email;
    private String status;

    public UserResponseDto(UUID id, String fullName, String email, String status) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.status = status;
    }

    public UUID getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getStatus() { return status; }
}
