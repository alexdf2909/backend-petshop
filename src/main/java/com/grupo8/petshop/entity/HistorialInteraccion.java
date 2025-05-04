package com.grupo8.petshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "HISTORIAL_INTERACCION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialInteraccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historialId;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private String tipoInteraccion; // VISTA, CLIC, COMPRA

    @Column(nullable = false)
    private LocalDateTime fecha;
}