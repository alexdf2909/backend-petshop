package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Mascota;
import com.grupo8.petshop.entity.Pedido;
import com.grupo8.petshop.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario(Usuario usuario);
}
