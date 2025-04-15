package com.grupo8.petshop.dto;

import lombok.Data;

@Data
public class VerificationRequest {
    private String correo;
    private String codigo;
}

