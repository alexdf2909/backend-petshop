package com.grupo8.petshop.dto.utils;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoMasVendidoDTO {
    private Long productoId;
    private String nombreProducto;
    private String categoria;
    private Long cantidadVendida;
}
