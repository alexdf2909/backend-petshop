package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.auth.LoginRequest;
import com.grupo8.petshop.dto.auth.LoginResponse;
import com.grupo8.petshop.dto.auth.RegisterRequest;
import com.grupo8.petshop.dto.auth.VerificationRequest;
import com.grupo8.petshop.entity.Usuario;
import com.grupo8.petshop.repository.IUsuarioRepository;
import com.grupo8.petshop.security.JwtUtil;
import com.grupo8.petshop.service.IAuthService;
import com.grupo8.petshop.util.Rol;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final IUsuarioRepository IUsuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final JwtUtil jwtUtil;

    public void register(RegisterRequest request) {
        if (IUsuarioRepository.existsByCorreo(request.getCorreo()))
            throw new RuntimeException("Correo ya registrado");

        String codigo = String.format("%06d", new Random().nextInt(999999));

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .contrasena(passwordEncoder.encode(request.getContrasena()))
                .rol(Rol.CLIENTE)
                .codigoVerificacion(codigo)
                .verificado(false)
                .isDeleted(false)
                .build();

        IUsuarioRepository.save(usuario);

        enviarCorreo(usuario.getCorreo(), codigo);
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = IUsuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena()))
            throw new RuntimeException("Credenciales inválidas");

        if (!usuario.getVerificado())
            throw new RuntimeException("Cuenta no verificada");

        String accessToken = jwtUtil.generateAccessToken(usuario.getUsuarioId(), usuario.getNombre(), usuario.getCorreo(), usuario.getRol().toString());
        String refreshToken = jwtUtil.generateRefreshToken(usuario.getCorreo(), usuario);

        // Opcional: guardar refreshToken en la DB
        usuario.setRefreshToken(refreshToken);
        IUsuarioRepository.save(usuario);

        return new LoginResponse(accessToken, refreshToken);
    }

    public void verificar(VerificationRequest request) {
        Usuario usuario = IUsuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getCodigoVerificacion().equals(request.getCodigo()))
            throw new RuntimeException("Código incorrecto");

        usuario.setVerificado(true);
        usuario.setCodigoVerificacion(null);
        IUsuarioRepository.save(usuario);
    }

    public LoginResponse refreshToken(String refreshToken) {
        String correo = jwtUtil.getCorreo(refreshToken);

        Usuario usuario = IUsuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si el refresh token es válido
        if (!refreshToken.equals(usuario.getRefreshToken())) {
            throw new RuntimeException("Refresh token inválido o expirado");
        }

        // Verificar si ha expirado el refresh token
        if (jwtUtil.isRefreshTokenExpired(usuario)) {
            throw new RuntimeException("Refresh token ha expirado");
        }

        String newAccessToken = jwtUtil.generateAccessToken(
                usuario.getUsuarioId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol().toString()
        );

        return new LoginResponse(newAccessToken, refreshToken);
    }

    public void enviarCorreo(String destino, String codigo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destino);
        mensaje.setSubject("Código de verificación");
        mensaje.setText("Tu código es: " + codigo);
        mailSender.send(mensaje);
    }
}

