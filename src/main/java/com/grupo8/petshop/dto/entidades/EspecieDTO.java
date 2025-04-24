package com.grupo8.petshop.dto.entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EspecieDTO {

    private Long especieId;

    private String nombre;

    private String imagenUrl;
}
