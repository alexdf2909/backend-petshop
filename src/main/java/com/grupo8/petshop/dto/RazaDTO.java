package com.grupo8.petshop.dto;

import com.grupo8.petshop.entity.Mascota;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RazaDTO {

    private Long razaId;

    private String nombre;

    private Long especieId;
}
