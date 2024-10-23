package org.example.exceptions;

public class RelatedEntityNotFoundException extends RuntimeException {

    public RelatedEntityNotFoundException(String message) {
        super(message);
    }

}