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

    public Optional<OpenIdClaims> validateCode(HttpServletRequest request, String code) {
        if (code == null) {
            return Optional.empty();
        }

        HttpSession session = request.getSession();
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return Optional.empty();
        }

        try {
            OpenIdClaims claims = this.googleApiService.getUserInfo(accessToken);
            return Optional.of(claims);
        }
        catch (IOException ioException) {
            return Optional.empty();
        }
    }
}
