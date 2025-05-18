package com.grupo8.petshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "VARIANTE")
public class Variante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long varianteId;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = true)
    private Color color;

    @ManyToOne
    @JoinColumn(name = "talla_id", nullable = true)
    private Talla talla;

    @ManyToOne
    @JoinColumn(name = "peso_id", nullable = true)
    private Peso peso;

    @Column(nullable = false)
    private float precioOriginal;

    @Column(nullable = false)
    private float precioOferta;

    @Column(nullable = false)
    private int stockMinimo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "variante_id", nullable = false)
    private Set<Imagen> imagenes = new HashSet<>();

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDeleted;
}
