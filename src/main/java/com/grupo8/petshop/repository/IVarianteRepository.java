package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IVarianteRepository extends JpaRepository<Variante, Long> {
    List<Variante> findByColor(Color color);
    List<Variante> findByTalla(Talla talla);
    List<Variante> findByPeso(Peso peso);
    List<Variante> findByProducto(Producto producto);
}
