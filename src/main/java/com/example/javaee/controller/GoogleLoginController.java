package com.example.javaee.controller;

import com.example.javaee.beans.AppConfigGoogleAccount;
import com.example.javaee.dto.AccessTokenResponse;
import com.example.javaee.service.GoogleApiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.Optional;

@Controller
public class GoogleLoginController {
    private final String state;
    private final GoogleApiService googleApiService;
    private final AppConfigGoogleAccount appConfigGoogleAccount;

    public GoogleLoginController(
            GoogleApiService googleApiService,
            AppConfigGoogleAccount appConfigGoogleAccount) {
        this.googleApiService = googleApiService;
        this.appConfigGoogleAccount = appConfigGoogleAccount;
        this.state = new BigInteger(130, new SecureRandom()).toString(32);
    }

    @GetMapping("/login.htm")
    public String loginRoute(ModelMap modelMap) throws URISyntaxException {
        final String API_END_POINT = "https://accounts.google.com/o/oauth2/v2/auth";
        final String REDIRECT_URI = "http://localhost:8080/javaee_war_exploded/login/LoginGoogleHandler.htm";

        String uri = UriComponentsBuilder.fromHttpUrl(API_END_POINT)
                .queryParam("response_type", "code")
                .queryParam("client_id", this.appConfigGoogleAccount.getClientId())
                .queryParam("nonce", this.appConfigGoogleAccount.getClientSecret())
                .queryParam("scope", "openid%20email%20profile")
                .queryParam("redirect_uri", REDIRECT_URI)
                .queryParam("state", this.state)
                .build()
                .toString();

        return "redirect:" + uri;
    }

    @GetMapping("/login/LoginGoogleHandler.htm")
    public String handleRedirectRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String receivedState = request.getParameter("state");
        if (!receivedState.equals(this.state)) {
            System.out.println("[Google Login Controller] Bad request ? Invalid state - Authorization session failed! -> Direct back to landing page.");
            return "redirect:/index";
        }

        HttpSession session = request.getSession();
        Optional<AccessTokenResponse> accessTokenResponse = this.googleApiService.getToken(code);
        if (!accessTokenResponse.isPresent()) {
            System.out.println("[Google Login Controller] Bad request ? Invalid code - Cannot fetch access token using given code! -> Direct back to landing page.");
            return "redirect:/index";
        }
        session.setAttribute("accessToken", accessTokenResponse.get().getAccessToken());
        session.setAttribute("expireIn", accessTokenResponse.get().getExpireIn());
        return "redirect:/admin/index.htm";
    }

}
