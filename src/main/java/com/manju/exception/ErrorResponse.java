package com.manju.exception;

public class ErrorResponse {
    private String message;
    private int code;
    private String details;

    public ErrorResponse(String message, int code, String details) {
        this.message = message;
        this.code = code;
        this.details = details;
    }

    // Getters and Setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

