package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.LoginRequest;
import com.grupo8.petshop.dto.RegisterRequest;
import com.grupo8.petshop.dto.VerificationRequest;
import com.grupo8.petshop.service.imp.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Usuario registrado. Revisa tu correo.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @PostMapping("/verificar")
    public ResponseEntity<?> verificar(@RequestBody VerificationRequest request) {
        authService.verificar(request);
        return ResponseEntity.ok("Cuenta verificada.");
    }
}
