package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Compra;
import com.grupo8.petshop.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
}
