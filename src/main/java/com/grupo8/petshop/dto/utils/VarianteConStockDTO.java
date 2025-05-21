package com.grupo8.petshop.dto.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VarianteConStockDTO {
    private Long varianteId;
    private String nombreProducto;
    private int stockUtil;
    private int stockVencido;
    private int stockMinimo;
    private boolean sinLotes;
}