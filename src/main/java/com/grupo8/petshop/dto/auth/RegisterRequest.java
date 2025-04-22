package com.grupo8.petshop.dto.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nombre;
    private String correo;
    private String contrasena;
}

