package com.pm.authservice.dto;

import java.util.UUID;

public class LoginResponseDTO {
    private final String token;
    private final String name;
    private UUID id;

    public LoginResponseDTO(String token, String name, UUID id) {
        this.token = token;
        this.name = name;
        this.id = id;
    }

    public String getToken() {
        return token;
    }
    public String getName() {return name;}
    public UUID getId() {return id;}
}
