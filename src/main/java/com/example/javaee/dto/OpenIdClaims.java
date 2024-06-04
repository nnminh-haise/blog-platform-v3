package com.example.javaee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenIdClaims {
    private String sub;

    private String email;

    private String email_verified;

    private String name;

    private String given_name;

    private String family_name;

    private String picture;
}
