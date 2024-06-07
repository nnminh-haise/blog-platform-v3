package com.example.javaee.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryResponse<T> extends BasicResponse <T> {
    private RepositoryErrorType repositoryErrorType;

    public static <T> RepositoryResponse<T> goodResponse(String message, T data) {
        RepositoryResponse<T> response = new RepositoryResponse<>();
        response.setResponseType(ResponseType.SUCCESS);
        response.setRepositoryErrorType(RepositoryErrorType.NO_ERROR);
        response.setData(Optional.ofNullable(data));
        response.setMessage(message);
        response.setDescription(message);
        return response;
    }

    public static <T> RepositoryResponse<T> badResponse(
            RepositoryErrorType repositoryErrorType, String message, String description) {
        RepositoryResponse<T> response = new RepositoryResponse<>();
        response.setResponseType(ResponseType.ERROR);
        response.setRepositoryErrorType(repositoryErrorType);
        response.setData(Optional.empty());
        response.setMessage(message);
        response.setDescription(description);
        return response;
    }

    public Boolean isGoodResponse() {
        if (this.isError()) {
            return  false;
        }

        if (!this.getRepositoryErrorType().equals(RepositoryErrorType.NO_ERROR)) {
            return false;
        }

        return  true;
    }

    public Boolean hasErrorOf(RepositoryErrorType repositoryErrorType) {
        if (!this.isError()) {
            return false;
        }

        return this.getRepositoryErrorType().equals(repositoryErrorType);
    }
}
