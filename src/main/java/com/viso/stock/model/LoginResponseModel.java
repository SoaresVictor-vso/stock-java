package com.viso.stock.model;

public class LoginResponseModel {
    final private String accessToken;
    final private String refreshToken;

    public LoginResponseModel(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
}
