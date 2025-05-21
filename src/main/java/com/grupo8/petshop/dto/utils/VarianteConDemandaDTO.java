package com.grupo8.petshop.dto.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class VarianteConDemandaDTO {
    private Long varianteId;
    private String nombre;
    private int stockUtil;
    private int stockVencido;
    private int stockMinimo;
    private boolean sinLotes;
    private int cantidadRecurrenteSugerida;
    private LocalDate fechaProximaRecompra;
    private List<UsuarioFrecuenteDTO> usuariosFrecuentes;
}