package com.perepilka.todo_backend.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private int statusCode;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
