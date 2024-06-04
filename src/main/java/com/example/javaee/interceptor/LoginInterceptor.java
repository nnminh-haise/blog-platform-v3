package com.example.javaee.interceptor;

import com.example.javaee.beans.AppConfigGoogleAccount;
import com.example.javaee.beans.SignInGoogleAccount;
import com.example.javaee.dto.AccessTokenResponse;
import com.example.javaee.dto.OpenIdClaims;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

public class LoginInterceptor implements HandlerInterceptor {
    private final SignInGoogleAccount signInGoogleAccount;

    private final AppConfigGoogleAccount appConfigGoogleAccount;

    public LoginInterceptor(
            AppConfigGoogleAccount appConfigGoogleAccount,
            SignInGoogleAccount signInGoogleAccount) {
        this.appConfigGoogleAccount = appConfigGoogleAccount;
        this.signInGoogleAccount = signInGoogleAccount;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        System.out.println("This message before execute the controller");

        String code = request.getParameter("code");
        if (code == null) {
            this.redirectTo(request, response, "/index.htm");
            return false;
        }

        System.out.println("code:" + code);
        AccessTokenResponse accessTokenResponse = getToken(code);
        System.out.println("accessToken:" + accessTokenResponse);
        HttpSession session = request.getSession();
        session.setAttribute("accessToken", accessTokenResponse.getAccessToken());
        OpenIdClaims claims = getUserInfo(accessTokenResponse.getAccessToken());
        System.out.println("claims:" + claims);
        boolean comparision = claims.getEmail().equals(this.signInGoogleAccount.getEmail());
        if (!comparision) {
            this.redirectTo(request, response, "/index.htm");
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {
        System.out.println("This message after controller and before execute the view");
        // your code
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler, Exception ex) {
        System.out.println("This message after execute the view");
        // your code
    }


    /**
     * This method use the code from the redirect request of Google to request the
     * access token from Google for fetching User's Information
     *
     * @param code
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    private AccessTokenResponse getToken(String code) throws ClientProtocolException, IOException {
        final String API_END_POINT = "https://oauth2.googleapis.com/token";
        final String GRAND_TYPE = "authorization_code";

        String response = Request.Post(API_END_POINT)
                .bodyForm(Form.form()
                        .add("client_id", this.appConfigGoogleAccount.getClientId())
                        .add("client_secret", this.appConfigGoogleAccount.getClientSecret())
                        .add("redirect_uri", this.appConfigGoogleAccount.getRedirectUri())
                        .add("code", code)
                        .add("grant_type", GRAND_TYPE).build())
                .execute()
                .returnContent()
                .asString();

        JsonObject responseAsJson = new Gson().fromJson(response, JsonObject.class);
        System.out.println("Get access token response:" + responseAsJson);
        return new AccessTokenResponse(
                responseAsJson.get("access_token").toString().replaceAll("\"", ""),
                responseAsJson.get("expires_in").toString()
        );
    }

    /**
     * This method takes the access token provided by Google then use it to fetch
     * the user's information from the authorized Google account
     *
     * @param accessToken
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    private OpenIdClaims getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        final String API_END_POINT = "https://openidconnect.googleapis.com/v1/userinfo?scope=openid%20profile&access_token=";
        String link = API_END_POINT + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        System.out.println("response:" + response);
        return new Gson().fromJson(response, OpenIdClaims.class);
    }

    private void redirectTo(HttpServletRequest request, HttpServletResponse response, String view) {
        final String CONTEXT_PATH = request.getContextPath();
        RedirectView redirectView = new RedirectView(CONTEXT_PATH + view);
        redirectView.setContextRelative(true);
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", redirectView.getUrl());
    }
}
