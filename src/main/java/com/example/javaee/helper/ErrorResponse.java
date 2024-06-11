package com.example.javaee.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String error;
    private String message;
    private String description;
    private String redirectLink;
    private String alertMessage;

    public ErrorResponse(String error, String message, String description) {
        this.error = error;
        this.message = message;
        this.description = description;
    }

    public static ErrorResponse buildUnauthorized(String message, String description) {
        return new ErrorResponse(
                ServiceErrorType.UNAUTHORIZED.toString(), message, description);
    }

    public static ErrorResponse buildForbidden(String message, String description) {
        return new ErrorResponse(
                ServiceErrorType.FORBIDDEN.toString(), message, description);
    }

    public static ErrorResponse buildBadRequest(String message, String description) {
        return new ErrorResponse(
                ServiceErrorType.BAD_REQUEST.toString(), message, description);
    }

    public static ErrorResponse buildUnknownServerError(String message, String description) {
        return new ErrorResponse(
                ServiceErrorType.UNKNOWN_SERVICE_ERROR.toString(), message, description);
    }

    public ErrorResponse redirectTo(String redirectLink) {
        this.redirectLink = redirectLink;
        return this;
    }

    public ErrorResponse thenAlert(String alertMessage) {
        this.alertMessage = alertMessage;
        return this;
    }
}
