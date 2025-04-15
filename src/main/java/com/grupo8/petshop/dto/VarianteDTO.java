package com.grupo8.petshop.dto;

import lombok.*;

import java.util.List;
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
    private Set<String> imagenes;
    private boolean isDeleted;
}
