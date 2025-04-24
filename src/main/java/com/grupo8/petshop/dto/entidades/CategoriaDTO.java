package com.grupo8.petshop.dto.entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CategoriaDTO {
    private Long categoriaId;

    private String nombre;

    private String imagenUrl;
}
