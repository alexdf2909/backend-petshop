package com.grupo8.petshop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ESPECIE")
public class Especie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long especieId;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String imagenUrl;
}
