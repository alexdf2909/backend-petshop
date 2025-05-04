package com.grupo8.petshop.entity;

import com.grupo8.petshop.util.EstadoPedido;
import com.grupo8.petshop.util.MetodoPago;
import com.grupo8.petshop.util.ModoEntrega;
import com.grupo8.petshop.util.ProgresoEntrega;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PEDIDO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pedidoId;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private float montoTotal;

    @Column
    private String direccionEnvio;

    @Enumerated(EnumType.STRING)
    private ModoEntrega modoEntrega; // ENVIO, RETIRO

    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago; // DEBITO, CREDITO, etc.

    @Enumerated(EnumType.STRING)
    private EstadoPedido estadoPedido; // PENDIENTE, EN_CAMINO, ENTREGADO, CANCELADO

    private String tiempoEstimadoEntrega;

    @Enumerated(EnumType.STRING)
    private ProgresoEntrega progresoEntrega; // EN_ESPERA, EN_CAMINO, ENTREGADO
}