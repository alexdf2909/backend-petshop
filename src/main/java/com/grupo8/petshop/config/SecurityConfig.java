package com.grupo8.petshop.config;

import com.grupo8.petshop.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

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

                        // Acceso solo para admin a /admin/**
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

