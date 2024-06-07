package com.example.javaee.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicResponse <T> {
    private ResponseType responseType;

    private String message;

    private String description;

    private Optional<T> data;

    public boolean isError() {
        return this.responseType == ResponseType.ERROR;
    }

    public boolean isSuccess() {
        return this.responseType == ResponseType.SUCCESS;
    }

    public boolean isEmpty() {
        return this.responseType == ResponseType.EMPTY;
    }

    public boolean hasData() {
        return this.data.isPresent();
    }
}
