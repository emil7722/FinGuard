package com.example.Finguard.common.dto;

import java.time.OffsetDateTime;

public class ErrorResponse {
    private int status;
    private String errorCode;
    private String message;
    private OffsetDateTime timestamp;

    public ErrorResponse(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = OffsetDateTime.now();
    }

    // Getters (required for JSON serialization)
    public int getStatus() { return status; }
    public String getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
    public OffsetDateTime getTimestamp() { return timestamp; }
}