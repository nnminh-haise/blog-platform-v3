package com.example.javaee.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppConfigGoogleAccount {
    private String clientId;

    private String clientSecret;

    private String redirectUri;
}
