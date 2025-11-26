package com.hipradeep.code.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private final String jwt;
    private final String refreshToken;

    public AuthResponse(String jwt, String refreshToken) {
        this.jwt = jwt;
        this.refreshToken = refreshToken;
    }
    public AuthResponse(String jwt) {
        this.jwt = jwt;
        this.refreshToken = null;
    }

    public String getJwt() { return jwt; }
    public String getRefreshToken() { return refreshToken; }
}