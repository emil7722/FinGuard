package com.example.Finguard.domain.user;

public class UserRequestDto {
    private String email;
    private String fullName;

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}