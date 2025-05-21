package com.grupo8.petshop.dto.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UsuarioFrecuenteDTO {
    private Long usuarioId;
    private String nombreUsuario;
    private int cantidadPromedio;
    private LocalDate fechaUltimaCompra;
    private LocalDate fechaProximaRecompra;
}
