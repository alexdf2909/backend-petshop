package com.grupo8.petshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "RAZA")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Raza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long razaId;

    @Column(nullable = false, unique = true)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "especie_id", nullable = true)
    private Especie especie;
}
