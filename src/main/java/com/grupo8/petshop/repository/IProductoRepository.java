package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(Categoria categoria);
    List<Producto> findByEspecie(Especie especie);
    List<Producto> findByMarca(Marca marca);
    List<Producto> findByEtiquetasContaining(Etiqueta etiqueta);
    @Query("SELECT c FROM Producto c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND c.isDeleted = false")
    List<Producto> findByNameContainingIgnoreCase(@Param("name") String name);
    @Query("""
        SELECT p FROM Producto p 
        LEFT JOIN p.etiquetas e 
        WHERE LOWER(p.especie.nombre) = LOWER(:especieNombre)
        AND p.isDeleted = false
        GROUP BY p
        ORDER BY 
            COUNT(CASE WHEN LOWER(e.nombre) IN :etiquetas THEN 1 END) DESC
        """)
    List<Producto> findRecomendadosPorEspecieYEtiquetas(String especieNombre, List<String> etiquetas);


}
