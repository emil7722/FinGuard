package com.example.Finguard.common.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends ApiException {
    public ValidationException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), "VALIDATION_ERROR", message);
    }
}