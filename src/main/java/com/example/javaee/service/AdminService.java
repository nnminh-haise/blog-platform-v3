package com.example.javaee.service;

import com.example.javaee.dto.OpenIdClaims;
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

    public Optional<OpenIdClaims> validateRequest(HttpServletRequest request) {
        if (request == null) {
            return Optional.empty();
        }

        HttpSession session = request.getSession();
        String accessToken = String.valueOf(session.getAttribute("accessToken"));
        String expireIn = String.valueOf(session.getAttribute("expireIn"));
        if (accessToken == null || expireIn == null) {
            return Optional.empty();
        }

        try {
            OpenIdClaims claims = this.googleApiService.getUserInfo(accessToken);
            return Optional.of(claims);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
