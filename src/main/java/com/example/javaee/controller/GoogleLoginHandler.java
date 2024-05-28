package com.example.javaee.controller;

import com.example.javaee.constants.GoogleUserConstant;
import com.example.javaee.dto.GoogleUserDto;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

@WebServlet(urlPatterns = "/login/LoginGoogleHandler.htm")
public class GoogleLoginHandler extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        System.out.println("code: " + code);
        String accessToken = getToken(code);
        System.out.println("access token: " + accessToken);
        GoogleUserDto user = getUserInfo(accessToken);
        System.out.println(user);
    }

    /**
     * This static method will make an API call to Google to get the token
     */
    public static String getToken(String code) throws ClientProtocolException, IOException {
        String response = Request.Post(GoogleUserConstant.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form().add("client_id", GoogleUserConstant.GOOGLE_CLIENT_ID)
                        .add("client_secret", GoogleUserConstant.GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", GoogleUserConstant.GOOGLE_REDIRECT_URI).add("code", code)
                        .add("grant_type", GoogleUserConstant.GOOGLE_GRANT_TYPE).build())
                .execute().returnContent().asString();

        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
        return accessToken;
    }

    public static GoogleUserDto getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = GoogleUserConstant.GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();

//        JsonObject userJson = new JsonObject().;

        System.out.println("response: " + response);

        GoogleUserDto googlePojo = new Gson().fromJson(response, GoogleUserDto.class);

        return googlePojo;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
