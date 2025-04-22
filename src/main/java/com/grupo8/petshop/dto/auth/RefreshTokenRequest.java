package com.grupo8.petshop.dto.auth;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
