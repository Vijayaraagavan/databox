package com.vijay.databox.exception;

import java.util.Map;

public class UserValidationException extends RuntimeException {
    private Map<String, String> errors;
    public UserValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
    public Map<String, String> getErrors() {
        return errors;
    }
}
