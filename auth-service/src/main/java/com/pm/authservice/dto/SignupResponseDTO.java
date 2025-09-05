package com.pm.authservice.dto;

import java.util.UUID;

public class SignupResponseDTO {
    private final String token;
    private final String name;
    private UUID id;

    public SignupResponseDTO(String token, String name) {
        this.token = token;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }
}
