package com.grupo8.petshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICACION_CLIENTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificacionId;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @Column(nullable = false)
    private String mensaje;

    @Column(nullable = false)
    private boolean leido;

    @Column(nullable = false)
    private LocalDateTime fechaEnvio;
}

