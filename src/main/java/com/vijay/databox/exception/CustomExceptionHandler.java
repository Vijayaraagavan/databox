package com.vijay.databox.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    
    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<com.vijay.databox.exception.ErrorResponse> handleException(UserExistException ex) {
        return new ResponseEntity<>(new com.vijay.databox.exception.ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<com.vijay.databox.exception.ErrorResponse> handleException(CustomException ex) {
        return new ResponseEntity<>(new com.vijay.databox.exception.ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<Map<String, String>> handleException(UserValidationException ex) {
        return new ResponseEntity<>(ex.getErrors(), HttpStatus.BAD_REQUEST);
    }
}
