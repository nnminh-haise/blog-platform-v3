package com.example.javaee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUserDto {
    private String id;

    private String email;

    private String verified_email;

    private String name;

    private String given_name;

    private String family_name;

    private String picture;
}
