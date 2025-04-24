package com.grupo8.petshop.dto.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private String nombre;
    private String correo;
    private String telefono;

}
