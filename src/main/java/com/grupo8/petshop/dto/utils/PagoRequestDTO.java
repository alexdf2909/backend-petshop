package com.grupo8.petshop.dto.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagoRequestDTO {
    private List<CartItemDTO> carrito;
    private PaymentFormDTO pago;
    private Long usuarioId;
    private String direccionEnvio;
    private String modoEntrega; // "ENVIO" o "RETIRO"
    private float montoTotal;
}

