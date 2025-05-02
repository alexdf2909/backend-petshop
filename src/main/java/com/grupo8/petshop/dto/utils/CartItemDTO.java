package com.grupo8.petshop.dto.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private int cantidad;
    private String imagen;
    private String nombre;
    private float precioUnitario;
    private Long productoId;
    private Long varianteId;
}
