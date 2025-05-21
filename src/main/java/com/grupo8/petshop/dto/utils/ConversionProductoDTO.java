package com.grupo8.petshop.dto.utils;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversionProductoDTO {
    private Long productoId;
    private String nombreProducto;
    private Long vistas;
    private Long agregadosAlCarrito;
    private Long compras;
}
