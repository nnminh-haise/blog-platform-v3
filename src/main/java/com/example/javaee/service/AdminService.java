package com.example.javaee.service;

import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.helper.ServiceResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class AdminService {
    private final GoogleApiService googleApiService;

    public AdminService(GoogleApiService googleApiService) {
        this.googleApiService = googleApiService;
    }

    public ServiceResponse<OpenIdClaims> validateRequest(HttpServletRequest request) {
        if (request == null) {
            return ServiceResponse.ofBadRequest(
                    "No HttpServletRequest Provided",
                    "Cannot found any HttpServletRequest");
        }

        HttpSession session = request.getSession();
        String accessToken = String.valueOf(session.getAttribute("accessToken"));
        String expireIn = String.valueOf(session.getAttribute("expireIn"));
        if (accessToken == null || expireIn == null) {
            return ServiceResponse.ofBadRequest(
                    "No Access Token or Expire Time Found",
                    "Cannot found any access token or expire time");
        }

        try {
            OpenIdClaims claims = this.googleApiService.getUserInfo(accessToken);
            return ServiceResponse.ofSuccess(
                    "OpenID Claims found", "Authorized User!", claims);
        } catch (IOException e) {
            return ServiceResponse.ofUnknownServerError(
                    e.getMessage(),
                    e.getLocalizedMessage());
        }
    }
}
