package com.renault.garagemanager.exception;
/**
 * Exception levee lorsqu'une ressource demandee n'existe pas.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
