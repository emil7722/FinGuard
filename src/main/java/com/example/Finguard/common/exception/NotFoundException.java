package com.example.Finguard.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException(String resourceType, String id) {
        super(HttpStatus.NOT_FOUND.value(), "RESOURCE_NOT_FOUND",
                resourceType + " not found with id: " + id);
    }
}