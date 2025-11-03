package com.viso.stock.infra;

import org.postgresql.util.PSQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.viso.stock.exceptions.BadCredentialsException;
import com.viso.stock.exceptions.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseEntity> handleNotFoundException(NotFoundException ex) {
        System.err.println("Not Found error occurred: " + ex.getMessage());
        return new ResponseEntity<>(
            new ErrorResponseEntity(ex.getMessage(), ex.getSource(), ex.getCode()),
            org.springframework.http.HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<ErrorResponseEntity> handlePSQLException(PSQLException ex) {
        System.err.println("Database error occurred: " + ex.getMessage());
        return new ResponseEntity<>(
            new ErrorResponseEntity("Database Error", null, "DATABASE_ERROR"),
            org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseEntity> handleNotReadable(HttpMessageNotReadableException ex) {
        System.err.println("JSON parse error occurred: " + ex.getMessage());
        return new ResponseEntity<>(
            new ErrorResponseEntity("Malformed object", null, "BAD_REQUEST"),
            org.springframework.http.HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseEntity> handleBadCredentialsException(BadCredentialsException ex) {
        System.err.println("Bad credentials error occurred: " + ex.getMessage());
        return new ResponseEntity<>(
            new ErrorResponseEntity("Invalid Credentials", "auth", "BAD_CREDENTIALS"),
            org.springframework.http.HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseEntity> handleRuntimeException(RuntimeException ex) {
        System.err.println("Runtime error occurred: " + ex.getMessage());
        System.out.println(ex.getClass().getName());
        return new ResponseEntity<>(
            new ErrorResponseEntity("Internal Server Error", null, "RUNTIME_ERROR"),
            org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
