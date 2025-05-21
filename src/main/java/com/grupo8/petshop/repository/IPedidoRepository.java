package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Pedido;
import com.grupo8.petshop.entity.Usuario;
import com.grupo8.petshop.util.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario(Usuario usuario);
    @Query("SELECT p.estadoPedido, COUNT(p) FROM Pedido p GROUP BY p.estadoPedido")
    List<Object[]> countPedidosByEstado();

    @Query("SELECT SUM(p.montoTotal) FROM Pedido p WHERE p.estadoPedido = :estado")
    Float sumMontoTotalByEstado(@Param("estado") EstadoPedido estado);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.fechaRegistro BETWEEN :start AND :end")
    Long countByFechaRegistroBetween(LocalDateTime start, LocalDateTime end);

    // DetallePedidoRepository
    @Query("SELECT dp.producto.nombre, SUM(dp.cantidad) as total FROM DetallePedido dp GROUP BY dp.producto ORDER BY total DESC")
    List<Object[]> findTopProductosVendidos();

    // HistorialInteraccionRepository
    @Query("""
   SELECT hi.producto.nombre,
          SUM(CASE WHEN hi.tipoInteraccion = 'VISTA' THEN 1 ELSE 0 END),
          SUM(CASE WHEN hi.tipoInteraccion = 'CARRITO' THEN 1 ELSE 0 END),
          SUM(CASE WHEN hi.tipoInteraccion = 'COMPRA' THEN 1 ELSE 0 END)
   FROM HistorialInteraccion hi
   GROUP BY hi.producto.nombre
""")
    List<Object[]> getConversionesPorProducto();
    @Query("SELECT p.estadoPedido, COUNT(p) FROM Pedido p WHERE p.fechaRegistro BETWEEN :start AND :end GROUP BY p.estadoPedido")
    List<Object[]> countPedidosByEstadoBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    @Query("SELECT SUM(p.montoTotal) FROM Pedido p WHERE p.estadoPedido IN :estados AND p.fechaRegistro BETWEEN :start AND :end")
    Float sumMontoTotalByEstadoBetween(@Param("estados") List<EstadoPedido> estados,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);
}
