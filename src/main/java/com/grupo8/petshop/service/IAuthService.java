package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.LoginRequest;
import com.grupo8.petshop.dto.RegisterRequest;
import com.grupo8.petshop.dto.VerificationRequest;

public interface IAuthService {
    void register(RegisterRequest request);
    String login(LoginRequest request);
    void verificar(VerificationRequest request);
    void enviarCorreo(String destino, String codigo);
}
