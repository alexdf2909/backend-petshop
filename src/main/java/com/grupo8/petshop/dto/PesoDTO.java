package com.grupo8.petshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PesoDTO {

    private Long pesoId;

    private Float valor;

    private String medida;
}
