package com.grupo8.petshop.config;

import com.grupo8.petshop.security.CustomUserDetailsService;
import com.grupo8.petshop.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Cualquier usuario tiene acceso
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/producto/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/variante/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categoria/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/marca/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/especie/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/etiqueta/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/imagen/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/lote/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/peso/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/servicio/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/talla/**").permitAll()

                        // Solo ADMIN puede usar POST, PUT, DELETE en /producto/**
                        .requestMatchers(HttpMethod.POST, "/producto/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/producto/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/producto/**").hasRole("ADMIN")

                        // Solo ADMIN puede usar POST, PUT, DELETE en /variante/**
                        .requestMatchers(HttpMethod.POST, "/variante/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/variante/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/variante/**").hasRole("ADMIN")

                        // Solo ADMIN puede usar POST, PUT, DELETE en /lote/**
                        .requestMatchers(HttpMethod.POST, "/lote/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/lote/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/lote/**").hasRole("ADMIN")

                        // Solo ADMIN puede usar POST, PUT, DELETE en /compra/**
                        .requestMatchers(HttpMethod.POST, "/compra/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/compra/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/compra/**").hasRole("ADMIN")

                        // Solo ADMIN puede usar POST, PUT, DELETE en /categoria/**
                        .requestMatchers(HttpMethod.POST, "/categoria/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/categoria/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/categoria/**").hasRole("ADMIN")

                        // Solo ADMIN puede usar POST, PUT, DELETE en /color/**
                        .requestMatchers(HttpMethod.POST, "/color/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/color/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/color/**").hasRole("ADMIN")

                        // Solo ADMIN puede usar POST, PUT, DELETE en /especie/**
                        .requestMatchers(HttpMethod.POST, "/especie/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/especie/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/especie/**").hasRole("ADMIN")

                        // Solo ADMIN puede usar POST, PUT, DELETE en /etiqueta/**
                        .requestMatchers(HttpMethod.POST, "/etiqueta/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/etiqueta/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/etiqueta/**").hasRole("ADMIN")

                        // Solo ADMIN puede usar POST, PUT, DELETE en /imagen/**
                        .requestMatchers(HttpMethod.POST, "/imagen/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/imagen/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/imagen/**").hasRole("ADMIN")

                        // Solo ADMIN puede usar POST, PUT, DELETE en /marca/**
                        .requestMatchers(HttpMethod.POST, "/marca/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/marca/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/marca/**").hasRole("ADMIN")

                        // Solo ADMIN puede usar POST, PUT, DELETE en /peso/**
                        .requestMatchers(HttpMethod.POST, "/peso/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/peso/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/peso/**").hasRole("ADMIN")

                        // Solo ADMIN puede usar POST, PUT, DELETE en /servicio/**
                        .requestMatchers(HttpMethod.POST, "/servicio/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/servicio/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/servicio/**").hasRole("ADMIN")

                        // Solo ADMIN puede usar POST, PUT, DELETE en /talla/**
                        .requestMatchers(HttpMethod.POST, "/talla/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/talla/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/talla/**").hasRole("ADMIN")

                        // Permitir que cualquier usuario autenticado actualice su perfil
                        .requestMatchers(HttpMethod.PUT, "/usuario/perfil").authenticated()

                        // Solo ADMIN puede usar POST, PUT, DELETE en /usuario/**
                        .requestMatchers(HttpMethod.POST, "/usuario/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/usuario/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/usuario/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/raza/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/raza/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/raza/**").hasRole("ADMIN")

                        // Solo ADMIN puede usar POST, PUT, DELETE en /producto/**
                        .requestMatchers(HttpMethod.GET, "/mascota/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/recomendaciones/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/mascota/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/mascota/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/mascota/**").hasAnyRole("ADMIN", "CLIENTE")


                        // Todo lo demás requiere autenticación
                        .anyRequest().permitAll()
                )
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

