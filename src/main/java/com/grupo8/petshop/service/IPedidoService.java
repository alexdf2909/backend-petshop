package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.entidades.PedidoDTO;
import com.grupo8.petshop.entity.Mascota;
import com.grupo8.petshop.entity.Pedido;
import com.grupo8.petshop.entity.Variante;

import java.util.List;
import java.util.Optional;

public interface IPedidoService {
    Optional<Pedido> searchForId(Long id);
    List<Pedido> searchAll();
    void updatePedido(Long id, PedidoDTO pedidoDTO);
    List<Pedido> searchByUsuario(Long usuarioId);
    List<Pedido> obtenerPedidosDelUsuarioAutenticado();
}
