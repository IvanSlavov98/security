package com.example.security.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {

    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }

}
