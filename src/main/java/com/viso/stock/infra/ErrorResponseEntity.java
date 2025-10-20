package com.viso.stock.infra;

public class ErrorResponseEntity {
    private String message;
    private String source;
    private String code;

    public ErrorResponseEntity(String message, String source, String code) {
        this.message = message;
        this.source = source;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public String getSource() {
        return source;
    }

    public String getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
