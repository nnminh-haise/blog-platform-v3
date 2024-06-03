package com.example.javaee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public
class AccessTokenResponse {
    private String accessToken;

    private String expireIn;
}
