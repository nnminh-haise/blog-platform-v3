package com.example.javaee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubscriberDto {
    @NotNull
    @Email
    private String email;

    @NotNull
    private String fullName;
}
