package com.grupo8.petshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "raza")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Raza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "raza_id")
    private Long razaId;

    @Column(nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "raza", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mascota> mascotas;
}
