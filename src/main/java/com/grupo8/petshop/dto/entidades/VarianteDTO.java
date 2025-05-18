package com.grupo8.petshop.dto.entidades;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VarianteDTO {
    private Long varianteId;
    private Long productoId;
    private Long colorId;
    private Long tallaId;
    private Long pesoId;
    private float precioOriginal;
    private float precioOferta;
    private int stockMinimo;
    private Set<String> imagenes;
    private boolean isDeleted;
}
