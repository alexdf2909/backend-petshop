package com.grupo8.petshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {

    private Long servicioId;

    private String nombre;

    private String descripcion;

    private String imagenAntes;

    private String imagenDespues;

    private String horario;
}
