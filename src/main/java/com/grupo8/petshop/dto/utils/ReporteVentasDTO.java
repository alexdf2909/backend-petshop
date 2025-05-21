package com.grupo8.petshop.dto.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteVentasDTO {
    private long ventasHoy;
    private Map<String, Long> pedidosPorEstado;
    private float montoTotal;
    private List<ProductoMasVendidoDTO> productosTop;
    private List<ConversionProductoDTO> conversionesPorProducto;
    private List<VentasProductoDTO> ventasPorProducto;
}
