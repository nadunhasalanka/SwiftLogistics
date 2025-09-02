package com.pm.authservice.dto;

public class SignupResponseDTO {
    private final String token;

    public SignupResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
