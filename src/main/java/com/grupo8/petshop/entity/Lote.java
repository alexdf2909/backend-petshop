package com.grupo8.petshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "LOTE")
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loteId;

    @ManyToOne
    @JoinColumn(name = "compra_id", nullable = false)
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "variante_id", nullable = false)
    private Variante variante;

    @Column(nullable = true)
    private LocalDate fechaVencimiento;

    @Column(nullable = false)
    private LocalDate fechaFabricacion;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDeleted;
}
