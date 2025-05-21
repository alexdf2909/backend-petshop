package com.grupo8.petshop.dto.entidades;


import com.grupo8.petshop.util.TipoInteraccion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistorialInteraccionDTO {

    private Long productoId;

    private TipoInteraccion tipoInteraccion; // VISTA, CARRITO, COMPRA

}
