package com.grupo8.petshop.dto.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String correo;
    private String contrasena;
}
