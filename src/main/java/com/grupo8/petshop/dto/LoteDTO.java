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
public class LoteDTO {

    private Long loteId;

    private Long compraId;

    private Long varianteId;

    private LocalDate fechaVencimiento;

    private LocalDate fechaFabricacion;

    private int stock;

    private boolean isDeleted;
}