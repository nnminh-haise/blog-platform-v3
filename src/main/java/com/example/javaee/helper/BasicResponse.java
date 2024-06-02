package com.example.javaee.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicResponse <T> {
    private ResponseType type;

    private String message;

    private String description;

    private Optional<T> data;

    public boolean isError() {
        return this.type == ResponseType.ERROR;
    }

    public boolean isSuccess() {
        return this.type == ResponseType.SUCCESS;
    }

    public boolean isEmpty() {
        return this.type == ResponseType.EMPTY;
    }

    public boolean hasData() {
        return this.data.isPresent();
    }
}
