package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.utils.ConversionProductoDTO;
import com.grupo8.petshop.dto.utils.ProductoMasVendidoDTO;
import com.grupo8.petshop.dto.utils.ReporteVentasDTO;
import com.grupo8.petshop.dto.utils.VentasProductoDTO;
import com.grupo8.petshop.repository.IDetallePedidoRepository;
import com.grupo8.petshop.repository.IHistorialInteraccionRepository;
import com.grupo8.petshop.repository.IPedidoRepository;
import com.grupo8.petshop.util.EstadoPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteVentasService {

    private final IPedidoRepository pedidoRepository;
    private final IDetallePedidoRepository detallePedidoRepository;
    private final IHistorialInteraccionRepository historialInteraccionRepository;

    public ReporteVentasDTO generarReporte(LocalDateTime inicio, LocalDateTime fin) {

        // Ventas del día (si no se especifica fecha, se asume hoy)
        LocalDateTime start = (inicio != null) ? inicio : LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime end = (fin != null) ? fin : LocalDateTime.now().toLocalDate().atTime(23, 59, 59);

        long ventasHoy = pedidoRepository.countByFechaRegistroBetween(start, end);

        //cantidad de pedidos por estado
        List<Object[]> estados = pedidoRepository.countPedidosByEstadoBetween(start, end);
        Map<String, Long> pedidosPorEstado = estados.stream()
                .collect(Collectors.toMap(
                        e -> ((EstadoPedido) e[0]).name(),
                        e -> (Long) e[1]
                ));

        List<EstadoPedido> estadosValidos = List.of(
                EstadoPedido.PENDIENTE,
                EstadoPedido.EN_PROGRESO,
                EstadoPedido.COMPLETADO
        );

        Float montoTotal = pedidoRepository.sumMontoTotalByEstadoBetween(estadosValidos, start, end);
        if (montoTotal == null) montoTotal = 0f;

        List<ProductoMasVendidoDTO> productosTop = detallePedidoRepository.obtenerProductosMasVendidos(start, end);

        List<ConversionProductoDTO> conversiones = historialInteraccionRepository.getConversionesPorProducto(start, end);

        List<VentasProductoDTO> ventasPorProducto = detallePedidoRepository.obtenerVentasPorProductoYCategoria(start, end);

        return ReporteVentasDTO.builder()
                .ventasHoy(ventasHoy)
                .pedidosPorEstado(pedidosPorEstado)
                .montoTotal(montoTotal)
                .productosTop(productosTop)
                .conversionesPorProducto(conversiones)
                .ventasPorProducto(ventasPorProducto)
                .build();
    }
}

