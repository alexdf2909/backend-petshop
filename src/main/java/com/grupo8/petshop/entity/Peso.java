package com.grupo8.petshop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "PESO")
public class Peso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pesoId;

    @Column(nullable = false)
    private String valor;
}
