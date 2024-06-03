package com.example.javaee.controller;

import com.example.javaee.beans.AppConfigGoogleAccount;
import com.example.javaee.beans.SignInGoogleAccount;
import com.example.javaee.dto.AccessTokenResponse;
import com.example.javaee.dto.OpenIdClaims;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

@Controller
public class GoogleLoginController {

    private final String state;

    private final AppConfigGoogleAccount appConfigGoogleAccount;

    private final SignInGoogleAccount signInGoogleAccount;

    public GoogleLoginController(
            SignInGoogleAccount signInGoogleAccount,
            AppConfigGoogleAccount appConfigGoogleAccount) {
        this.signInGoogleAccount = signInGoogleAccount;
        this.appConfigGoogleAccount = appConfigGoogleAccount;
        this.state = new BigInteger(130, new SecureRandom()).toString(32);
    }

    // TODO: clean this code
    @GetMapping("/login.htm")
    public String loginRoute(ModelMap modelMap) {
        String link = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "response_type=code&" +
                "client_id=538789814527-5g9809t6hdge3qrh5jjhp3q3ab3vb9vd.apps.googleusercontent.com&" +
                "nonce=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&" +
                "scope=openid%20email&" +
                "redirect_uri=http://localhost:8080/javaee_war_exploded/login/LoginGoogleHandler.htm&" +
                "state=" + this.state;

        return "redirect:" + link;
    }

    @GetMapping("/login/LoginGoogleHandler.htm")
    public String handleRedirectRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String receivedState = request.getParameter("state");
        String scope = request.getParameter("scope");
        System.out.println("code:" + code);
        System.out.println("receivedState:" + receivedState);
        System.out.println("currentState :" + this.state);
        if (!receivedState.equals(this.state)) {
            System.out.println("Invalid state! Authorization session failed!");
            return "index";
        }
        AccessTokenResponse accessTokenResponse = getToken(code);
//        System.out.println("accessToken:" + accessTokenResponse);
        OpenIdClaims user = getUserInfo(accessTokenResponse.getAccessToken());
        if (!user.getEmail().equals(this.signInGoogleAccount.getEmail())) {
            System.out.println("Forbidden Google account!");
        }
        else {
            System.out.println("Authorized!");
        }
//        System.out.println(user);
        return "redirect:/index.htm";
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
}
