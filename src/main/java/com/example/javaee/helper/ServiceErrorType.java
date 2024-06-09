package com.example.javaee.helper;

public enum ServiceErrorType {
    NO_ERROR("No Error"),
    UNKNOWN_SERVICE_ERROR("Unknown Service Error"),
    BAD_REQUEST("Bad Request"),
    FORBIDDEN("Forbidden"),
    UNAUTHORIZED("Unauthorized"),
    NOT_FOUND("Not Found"),
    OK("OK"),
    CREATED("Created");

    private final String displayName;

    ServiceErrorType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
