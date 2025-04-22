package com.grupo8.petshop.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.grupo8.petshop.entity.Usuario;
import com.grupo8.petshop.repository.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private final IUsuarioRepository IUsuarioRepository;

    public String generateAccessToken(Long usuarioId, String nombre, String correo, String rol) {
        return JWT.create()
                .withSubject(correo)
                .withClaim("id", usuarioId)
                .withClaim("nombre", nombre)
                .withClaim("rol", rol)
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(Instant.now().plus(1, ChronoUnit.HOURS))) // 1 hora
                .sign(Algorithm.HMAC256(secret));
    }

    public String generateRefreshToken(String correo, Usuario usuario) {
        Date expiryDate = Date.from(Instant.now().plus(7, ChronoUnit.DAYS));  // Expira en 7 días

        // Guardamos la fecha de expiración en el usuario (si es necesario)
        usuario.setRefreshTokenExpiry(expiryDate);
        IUsuarioRepository.save(usuario);

        return JWT.create()
                .withSubject(correo)
                .withIssuedAt(new Date())
                .withExpiresAt(expiryDate)  // Establecer la expiración
                .sign(Algorithm.HMAC256(secret));
    }

    public void revokeRefreshToken(String correo) {
        Usuario usuario = IUsuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setRefreshToken(null);  // Limpiar el refresh token
        usuario.setRefreshTokenExpiry(null);  // Limpiar la fecha de expiración

        IUsuarioRepository.save(usuario);  // Guardar los cambios
    }

    public boolean isRefreshTokenExpired(Usuario usuario) {
        return usuario.getRefreshTokenExpiry().before(new Date());
    }


    public String getCorreo(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getSubject();
    }
}

