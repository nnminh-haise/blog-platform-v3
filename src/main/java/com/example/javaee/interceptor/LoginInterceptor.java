package com.example.javaee.interceptor;

import com.example.javaee.beans.SignInGoogleAccount;
import com.example.javaee.dto.AccessTokenResponse;
import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.service.GoogleApiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class LoginInterceptor implements HandlerInterceptor {
    private final SignInGoogleAccount signInGoogleAccount;

    private final GoogleApiService googleApiService;

    public LoginInterceptor(
            GoogleApiService googleApiService,
            SignInGoogleAccount signInGoogleAccount) {
        this.googleApiService = googleApiService;
        this.signInGoogleAccount = signInGoogleAccount;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        String code = request.getParameter("code");
        if (code == null) {
            this.redirectTo(request, response, "/index.htm");
            return false;
        }

        AccessTokenResponse accessTokenResponse = this.googleApiService.getToken(code);
        OpenIdClaims claims = this.googleApiService.getUserInfo(accessTokenResponse.getAccessToken());

        System.out.println("[Interceptor] code:" + code);
        System.out.println("[Interceptor] accessToken:" + accessTokenResponse);
        System.out.println("[Interceptor] claims:" + claims);

        boolean authorizeResult = claims.getEmail().equals(this.signInGoogleAccount.getEmail());
        if (!authorizeResult) {
            this.redirectTo(request, response, "/index.htm");
            return false;
        }

        HttpSession session = request.getSession();
        session.setAttribute("accessToken", accessTokenResponse.getAccessToken());
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler, Exception ex) {
    }


    private void redirectTo(HttpServletRequest request, HttpServletResponse response, String view) {
        final String CONTEXT_PATH = request.getContextPath();
        RedirectView redirectView = new RedirectView(CONTEXT_PATH + view);
        redirectView.setContextRelative(true);
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", redirectView.getUrl());
    }
}
