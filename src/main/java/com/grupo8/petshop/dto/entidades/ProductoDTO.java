package com.grupo8.petshop.dto.entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long productoId;

    private String nombre;

    private String descripcion;

    private Long especieId;

    private Long marcaId;

    private Long categoriaId;

    private Set<Long> etiquetaIds;

    private boolean isDeleted;
}