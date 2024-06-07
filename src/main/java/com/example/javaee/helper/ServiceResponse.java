package com.example.javaee.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Optional;

enum ServiceErrorType {
    NO_ERROR,
    UNKNOWN_SERVICE_ERROR,
    BAD_REQUEST,
    FORBIDDEN,
    UNAUTHORIZED,
    NOT_FOUND,
    OK,
    CREATED
}

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse<T> extends BasicResponse <T> {
    private ServiceErrorType error;

    public static <T> ServiceResponse<T> ofNotFound(String message, String description) {
        ServiceResponse<T> serviceResponse = new ServiceResponse<>();
        serviceResponse.setResponseType(ResponseType.ERROR);
        serviceResponse.setMessage(message);
        serviceResponse.setDescription(description);
        serviceResponse.setError(ServiceErrorType.NOT_FOUND);
        serviceResponse.setData(Optional.empty());
        return serviceResponse;
    }

    public static <T> ServiceResponse<T> ofUnauthorized(String message, String description) {
        ServiceResponse<T> serviceResponse = new ServiceResponse<>();
        serviceResponse.setResponseType(ResponseType.ERROR);
        serviceResponse.setMessage(message);
        serviceResponse.setDescription(description);
        serviceResponse.setError(ServiceErrorType.UNAUTHORIZED);
        serviceResponse.setData(Optional.empty());
        return serviceResponse;
    }

    public static <T> ServiceResponse<T> ofBadRequest(String message, String description) {
        ServiceResponse<T> serviceResponse = new ServiceResponse<>();
        serviceResponse.setResponseType(ResponseType.ERROR);
        serviceResponse.setMessage(message);
        serviceResponse.setDescription(description);
        serviceResponse.setError(ServiceErrorType.BAD_REQUEST);
        serviceResponse.setData(Optional.empty());
        return serviceResponse;
    }

    public static <T> ServiceResponse<T> ofUnknownServerError(String message, String description) {
        ServiceResponse<T> serviceResponse = new ServiceResponse<>();
        serviceResponse.setResponseType(ResponseType.ERROR);
        serviceResponse.setMessage(message);
        serviceResponse.setDescription(description);
        serviceResponse.setError(ServiceErrorType.UNKNOWN_SERVICE_ERROR);
        serviceResponse.setData(Optional.empty());
        return serviceResponse;
    }

    public static <T> ServiceResponse<T> ofNoError(String message, String description, T data) {
        ServiceResponse<T> serviceResponse = new ServiceResponse<>();
        serviceResponse.setResponseType(ResponseType.SUCCESS);
        serviceResponse.setMessage(message);
        serviceResponse.setDescription(description);
        serviceResponse.setError(ServiceErrorType.NO_ERROR);
        serviceResponse.setData(Optional.ofNullable(data));
        return serviceResponse;
    }

    public static <T> ServiceResponse<T> ofSuccess(String message, String description, T data) {
        ServiceResponse<T> serviceResponse = new ServiceResponse<>();
        serviceResponse.setResponseType(ResponseType.SUCCESS);
        serviceResponse.setMessage(message);
        serviceResponse.setDescription(description);
        serviceResponse.setError(ServiceErrorType.CREATED);
        serviceResponse.setData(Optional.ofNullable(data));
        return serviceResponse;
    }
}
