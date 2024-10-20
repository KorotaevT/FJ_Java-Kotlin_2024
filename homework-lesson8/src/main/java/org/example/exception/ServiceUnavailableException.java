package org.example.exception;

public class ServiceUnavailableException extends Exception {
    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}