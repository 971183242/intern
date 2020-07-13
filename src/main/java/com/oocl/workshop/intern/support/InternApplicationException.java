package com.oocl.workshop.intern.support;

public class InternApplicationException extends RuntimeException {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public InternApplicationException(String code) {
        this.code = code;
    }

    public InternApplicationException(String code, String message) {
        super(message);
        this.code = code;
    }

    public InternApplicationException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public InternApplicationException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }
}
