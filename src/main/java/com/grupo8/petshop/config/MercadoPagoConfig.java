package com.grupo8.petshop.config;

import com.mercadopago.client.preference.PreferenceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoConfig {

    @Value("${mercadopago.access.token}")
    private String accessToken; // Obtiene el token de acceso desde application.properties

    @Bean
    public PreferenceClient preferenceClient() {
        // Configuración del SDK de Mercado Pago
        System.setProperty("mercadopago.access.token", accessToken); // Configura el token de acceso

        return new PreferenceClient();
    }
}
