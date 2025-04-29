package com.grupo8.petshop.dto;


import com.grupo8.petshop.entity.Mascota;
import com.grupo8.petshop.util.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioDTO {

    private Long usuarioId;

    private String nombre;

    private String correo;

    private String contrasena;

    private Rol rol;

    private String codigoVerificacion;

    private Boolean verificado = false;

    private Set<ProductoDTO> favoritos;

    private boolean isDeleted;

    private String imagenPerfil;

    private Set<Mascota> mascotas;

}
