package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Producto;
import com.grupo8.petshop.entity.Usuario;
import com.grupo8.petshop.dto.utils.ConversionProductoDTO;
import com.grupo8.petshop.entity.*;
import com.grupo8.petshop.util.TipoInteraccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IHistorialInteraccionRepository extends JpaRepository<HistorialInteraccion, Long> {
    @Query("""
SELECT new com.grupo8.petshop.dto.utils.ConversionProductoDTO(
    hi.producto.productoId,
    hi.producto.nombre,
    SUM(CASE WHEN hi.tipoInteraccion = 'VISTA' THEN 1 ELSE 0 END),
    SUM(CASE WHEN hi.tipoInteraccion = 'CARRITO' THEN 1 ELSE 0 END),
    SUM(CASE WHEN hi.tipoInteraccion = 'COMPRA' THEN 1 ELSE 0 END)
)
FROM HistorialInteraccion hi
WHERE hi.fecha BETWEEN :start AND :end
GROUP BY hi.producto
""")
    List<ConversionProductoDTO> getConversionesPorProducto(@Param("start") LocalDateTime start,
                                                           @Param("end") LocalDateTime end);
    List<HistorialInteraccion> findByUsuario(Usuario usuario);
    List<HistorialInteraccion> findByProducto(Producto producto);

    List<HistorialInteraccion> findByUsuarioAndProductoAndTipoInteraccionOrderByFechaAsc(
            Usuario usuario,
            Producto producto,
            TipoInteraccion tipoInteraccion
    );
    boolean existsByUsuarioAndProductoAndFechaAfter(Usuario usuario, Producto producto, LocalDateTime fecha);

}
