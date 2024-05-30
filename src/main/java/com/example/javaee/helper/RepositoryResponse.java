package com.example.javaee.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryResponse<T> extends BasicResponse <T> {
    private RepositoryErrorType error;
}
