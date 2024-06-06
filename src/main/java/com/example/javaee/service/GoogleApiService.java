package com.example.javaee.service;

import com.example.javaee.beans.AppConfigGoogleAccount;
import com.example.javaee.dto.AccessTokenResponse;
import com.example.javaee.dto.OpenIdClaims;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class GoogleApiService {

    private final AppConfigGoogleAccount appConfigGoogleAccount;

    public GoogleApiService(AppConfigGoogleAccount appConfigGoogleAccount) {
        this.appConfigGoogleAccount = appConfigGoogleAccount;
    }

    public Optional<AccessTokenResponse> getToken(String code) throws IOException {
        final String API_END_POINT = "https://oauth2.googleapis.com/token";
        final String GRAND_TYPE = "authorization_code";

        try {
            String response = Request.Post(API_END_POINT)
                    .bodyForm(Form.form()
                            .add("client_id", this.appConfigGoogleAccount.getClientId())
                            .add("client_secret", this.appConfigGoogleAccount.getClientSecret())
                            .add("redirect_uri", this.appConfigGoogleAccount.getRedirectUri())
                            .add("code", code)
                            .add("grant_type", GRAND_TYPE)
                            .build())
                    .execute()
                    .returnContent()
                    .asString();

            JsonObject responseAsJson = new Gson().fromJson(response, JsonObject.class);
            return Optional.of(new AccessTokenResponse(
                    responseAsJson.get("access_token").toString().replaceAll("\"", ""),
                    responseAsJson.get("expires_in").toString()
            ));
        } catch (IOException ioException) {
            return Optional.empty();
        }
    }

    public OpenIdClaims getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        final String API_END_POINT = "https://openidconnect.googleapis.com/v1/userinfo?scope=openid%20profile&access_token=";
        String link = API_END_POINT + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        return new Gson().fromJson(response, OpenIdClaims.class);
    }
}
