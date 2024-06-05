package com.example.javaee.controller;

import com.example.javaee.beans.AppConfigGoogleAccount;
import com.example.javaee.beans.SignInGoogleAccount;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.SecureRandom;

// TODO: upgrade these Sysout to logging system
@Controller
public class GoogleLoginController {

    private final String state;

    private final AppConfigGoogleAccount appConfigGoogleAccount;

    public GoogleLoginController(
            AppConfigGoogleAccount appConfigGoogleAccount) {
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
        System.out.println("code:" + code);
        System.out.println("receivedState:" + receivedState);
        System.out.println("currentState :" + this.state);
        if (!receivedState.equals(this.state)) {
            System.out.println("Invalid state! Authorization session failed!");
            return "redirect:/index";
        }
        return "redirect:/admin/index.htm?code=" + code;
    }

}
