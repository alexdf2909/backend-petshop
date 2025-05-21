package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Producto;
import com.grupo8.petshop.entity.Recomendacion;
import com.grupo8.petshop.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IRecomendacionRepository extends JpaRepository<Recomendacion, Long> {
    Optional<Recomendacion> findByUsuarioAndProducto(Usuario usuario, Producto producto);
    List<Recomendacion> findByUsuario(Usuario usuario);
}
