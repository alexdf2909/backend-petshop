package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.auth.LoginRequest;
import com.grupo8.petshop.dto.auth.LoginResponse;
import com.grupo8.petshop.dto.auth.RegisterRequest;
import com.grupo8.petshop.dto.auth.VerificationRequest;
import com.grupo8.petshop.entity.Usuario;

public interface IAuthService {
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    void verificar(VerificationRequest request);
    void enviarCorreo(Usuario usuario, String codigo);
}
