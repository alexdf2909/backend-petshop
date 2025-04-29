package com.grupo8.petshop.config;

import com.mercadopago.client.preference.PreferenceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mercadopago.MercadoPagoConfig;


@Configuration
public class MercadoPagoConfiguration {

    @Value("${mercadopago.access.token}")
    private String accessToken; // Obtiene el token de acceso desde application.properties

    @Bean
    public PreferenceClient preferenceClient() {
        MercadoPagoConfig.setAccessToken(accessToken); // Ahora sí funciona
        return new PreferenceClient();
    }
}
