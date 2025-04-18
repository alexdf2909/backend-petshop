package com.grupo8.petshop.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompraDTO {

    private Long compraId;

    private String codigoComprobante;

    private String urlImagenComprobante;

    private LocalDate fechaCompra;

    private LocalDate fechaRegistro;

    private Long usuarioId;

    private boolean isDeleted;
}
