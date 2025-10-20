package com.viso.stock.exceptions;

public class NotFoundException extends RuntimeException {
    private final String source;
    private final String code = "NOT_FOUND";

    public NotFoundException(String message, String source) {
        super(message);
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public String getCode() {
        return code;
    }
}
