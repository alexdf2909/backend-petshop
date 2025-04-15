package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(Categoria categoria);
    List<Producto> findByEspecie(Especie especie);
    List<Producto> findByMarca(Marca marca);
    List<Producto> findByEtiquetasContaining(Etiqueta etiqueta);
}
