package com.viso.stock.exceptions;

public class BadCredentialsException extends org.springframework.security.authentication.BadCredentialsException {
    public BadCredentialsException(String msg) {
        super(msg);
    }
    
}
