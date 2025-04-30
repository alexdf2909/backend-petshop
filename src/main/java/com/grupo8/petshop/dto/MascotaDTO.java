package com.grupo8.petshop.dto;


import com.grupo8.petshop.entity.Raza;
import com.grupo8.petshop.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MascotaDTO {

    private Long mascotaId;

    private String nombre;

    private LocalDate fechaNacimiento;

    private Long especieId;

    private Long razaId;

    private String sexo;

    private String imagenUrl;

    private float peso;

    private Long usuarioId;
}
