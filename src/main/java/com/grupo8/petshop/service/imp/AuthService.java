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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

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

        enviarCorreo(usuario, codigo);
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

    public void enviarCorreo(Usuario usuario, String codigo) {
        MimeMessage mensaje = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            helper.setTo(usuario.getCorreo());
            helper.setSubject("Código de verificación");

            String logoUrl = "https://scontent.flim15-2.fna.fbcdn.net/v/t39.30808-6/459579588_122177535992191073_6501808402919543674_n.jpg?_nc_cat=107&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=BaapdhfNksgQ7kNvwF8GGWI&_nc_oc=AdnoIS82uAvYM6oqKshcK0a2Tlbfzb8VXYqoHotnqFQUSfMZggl1OZvixMRhoyyWdE7iAV4saFwxPqQQDiLIauM2&_nc_zt=23&_nc_ht=scontent.flim15-2.fna&_nc_gid=eXYZYkffa5lGcgIpUaJrcg&oh=00_AfG4MDSuUe0f7IyYvlfYcadQNrtWR69YbEsFGg31aWHpAA&oe=681CA409"; // Cambia esto por la URL de tu logo

            String contenidoHtml = "<div style=\"font-family: Arial, sans-serif; color: #333;\">"
                    + "<img src=\"" + logoUrl + "\" alt=\"Logo\" style=\"width: 150px; margin-bottom: 20px;\"/>"
                    + "<h2>Código de verificación</h2>"
                    + "<p>Hola " + usuario.getNombre() + ",</p>"
                    + "<p>Gracias por registrarte en <strong>Yoshi Pets</strong>. Para completar tu proceso de verificación e iniciar sesión, por favor utiliza el siguiente código:</p>"
                    + "<p style=\"font-size: 24px; font-weight: bold; color: #4CAF50;\">" + codigo + "</p>"
                    + "<p>Si no solicitaste este código, por favor ignora este correo.</p>"
                    + "<p>¡Gracias por confiar en nosotros!</p>"
                    + "</div>";

            helper.setText(contenidoHtml, true); // 'true' indica que es HTML

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            e.printStackTrace(); // Puedes agregar manejo de error más robusto
        }
    }
}

