package com.grupo8.petshop.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                var verifier = JWT.require(Algorithm.HMAC256(secret)).build();
                var jwt = verifier.verify(token);

                String correo = jwt.getSubject();
                String rol = jwt.getClaim("rol").asString(); // viene como "ADMIN"

                var authority = new SimpleGrantedAuthority("ROLE_" + rol); // Spring espera "ROLE_ADMIN"

                var authentication = new UsernamePasswordAuthenticationToken(
                        correo,
                        null,
                        Collections.singletonList(authority)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                System.out.println("Token inválido: " + e.getMessage());
                // puedes opcionalmente rechazar la request con 401
            }
        }

        filterChain.doFilter(request, response);
    }
}
