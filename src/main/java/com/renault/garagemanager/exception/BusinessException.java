package com.renault.garagemanager.exception;
/**
 * Thrown when a business rule or constraint is violated.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
