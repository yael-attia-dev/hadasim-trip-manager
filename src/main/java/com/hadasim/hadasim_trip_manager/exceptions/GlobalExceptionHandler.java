package com.hadasim.hadasim_trip_manager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /* this method catches our runtime errors like duplicate id */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeErrors(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("status", "error");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /* this method catches validation errors like empty names */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "check your data again");
        body.put("status", "validation failed");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}