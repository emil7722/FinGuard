package com.example.Finguard.common.exception;

import org.springframework.http.HttpStatus;

public class InsufficientFundsException extends ApiException {
    public InsufficientFundsException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), "INSUFFICIENT_FUNDS", message);
    }
}