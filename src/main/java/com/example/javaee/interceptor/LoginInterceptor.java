package com.example.javaee.interceptor;

import com.example.javaee.beans.SignInGoogleAccount;
import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.service.GoogleApiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    private final GoogleApiService googleApiService;
    private final SignInGoogleAccount signInGoogleAccount;

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
        HttpSession session = request.getSession();
        String accessToken = (String)session.getAttribute("accessToken");
        Integer expireIn = Integer.valueOf((String) session.getAttribute("expireIn"));
        if (accessToken == null) {
            System.out.println("[Interceptor] (preHandle) Bad request ? No access token -> Direct back to landing page");
            this.redirectToLandingPage(request, response);
            return false;
        }

        OpenIdClaims claims = this.googleApiService.getUserInfo(accessToken);
        boolean compareResult = claims.getEmail().equals(this.signInGoogleAccount.getEmail());
        if (!compareResult) {
            System.out.println("[Interceptor] (preHandle) Forbidden Account ? Email is not authorized -> Direct back to landing page");
            this.redirectToLandingPage(request, response);
            return false;
        }

        System.out.println("Passed interceptor checking");
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

    private void redirectToLandingPage(HttpServletRequest request, HttpServletResponse response) {
        final String CONTEXT_PATH = request.getContextPath();
        RedirectView redirectView = new RedirectView(CONTEXT_PATH + "/index.htm");
        redirectView.setContextRelative(true);
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", redirectView.getUrl());
    }
}
