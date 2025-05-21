package com.grupo8.petshop.repository;

import com.grupo8.petshop.dto.utils.ProductoMasVendidoDTO;
import com.grupo8.petshop.dto.utils.VentasProductoDTO;
import com.grupo8.petshop.entity.Compra;
import com.grupo8.petshop.entity.DetallePedido;
import com.grupo8.petshop.entity.Variante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IDetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    @Query("""
SELECT new com.grupo8.petshop.dto.utils.ProductoMasVendidoDTO(
    p.producto.productoId, 
    p.producto.nombre, 
    p.producto.categoria.nombre,
    SUM(p.cantidad)
)
FROM DetallePedido p
WHERE p.pedido.fechaRegistro BETWEEN :start AND :end
GROUP BY p.producto.productoId, p.producto.nombre, p.producto.categoria.nombre
ORDER BY SUM(p.cantidad) DESC
""")
    List<ProductoMasVendidoDTO> obtenerProductosMasVendidos(@Param("start") LocalDateTime start,
                                                            @Param("end") LocalDateTime end);

    @Query("""
SELECT new com.grupo8.petshop.dto.utils.VentasProductoDTO(
    p.producto.productoId,
    p.producto.nombre,
    p.producto.categoria.nombre,
    SUM(p.cantidad),
    SUM(p.subtotal)
)
FROM DetallePedido p
WHERE p.pedido.fechaRegistro BETWEEN :start AND :end
GROUP BY p.producto.productoId, p.producto.nombre, p.producto.categoria.nombre
ORDER BY SUM(p.subtotal) DESC
""")
    List<VentasProductoDTO> obtenerVentasPorProductoYCategoria(@Param("start") LocalDateTime start,
                                                               @Param("end") LocalDateTime end);
    List<DetallePedido> findByVarianteAndPedido_FechaRegistroAfter(Variante variante, LocalDateTime fecha);
}
