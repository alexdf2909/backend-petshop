package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILoteRepository extends JpaRepository<Lote, Long> {
    List<Lote> findByCompra(Compra compra);
    List<Lote> findByVariante(Variante variante);
    List<Lote> findByVarianteAndIsDeletedFalse(Variante variante);
    @Query("""
    SELECT SUM(l.stock)
    FROM Lote l
    WHERE l.variante.producto = :producto
    AND l.isDeleted = false
    AND l.variante.isDeleted = false
""")
    Integer obtenerStockTotalPorProducto(@Param("producto") Producto producto);
}
