package com.grupo8.petshop.dto;


import com.grupo8.petshop.entity.Raza;
import com.grupo8.petshop.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MascotaDTO {

    private Long mascotaId;

    private String nombre;

    private Date fechaNacimiento;

    private Raza raza;

    private Usuario usuario;


}
