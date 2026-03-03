package com.renault.garagemanager.exception;
/**
 * Exception levee lorsqu'une contrainte metier est violee.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
