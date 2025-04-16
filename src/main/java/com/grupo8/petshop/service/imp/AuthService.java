package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.LoginRequest;
import com.grupo8.petshop.dto.RegisterRequest;
import com.grupo8.petshop.dto.VerificationRequest;
import com.grupo8.petshop.entity.Usuario;
import com.grupo8.petshop.repository.IUsuarioRepository;
import com.grupo8.petshop.security.JwtUtil;
import com.grupo8.petshop.util.Rol;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

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

    public String login(LoginRequest request) {
        Usuario usuario = IUsuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena()))
            throw new RuntimeException("Credenciales inválidas");

        if (!usuario.getVerificado())
            throw new RuntimeException("Cuenta no verificada");

        return jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol().toString());
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

    private void enviarCorreo(String destino, String codigo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destino);
        mensaje.setSubject("Código de verificación");
        mensaje.setText("Tu código es: " + codigo);
        mailSender.send(mensaje);
    }
}

