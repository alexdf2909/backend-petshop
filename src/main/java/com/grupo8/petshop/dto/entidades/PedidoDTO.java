package com.grupo8.petshop.dto.entidades;

import com.grupo8.petshop.util.EstadoPedido;
import com.grupo8.petshop.util.MetodoPago;
import com.grupo8.petshop.util.ModoEntrega;
import com.grupo8.petshop.util.ProgresoEntrega;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private Long pedidoId;

    private Long usuarioId;

    private LocalDateTime fechaRegistro;

    private float montoTotal;

    private String direccionEnvio;

    private ModoEntrega modoEntrega; // ENVIO, RETIRO

    private MetodoPago metodoPago; // DEBITO, CREDITO

    private EstadoPedido estadoPedido; // PENDIENTE, EN_CAMINO, ENTREGADO, CANCELADO

    private String tiempoEstimadoEntrega;

    private ProgresoEntrega progresoEntrega; // EN_ESPERA, EN_CAMINO, ENTREGADO
}
