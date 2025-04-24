package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.entidades.CompraDTO;
import com.grupo8.petshop.entity.Compra;

import java.util.List;
import java.util.Optional;

public interface ICompraService {
    Compra createCompra(CompraDTO compraDTO);
    Optional<Compra> searchForId(Long id);
    List<Compra> searchAll();
    void updateCompra(Long id, CompraDTO compraDTO);
    void deleteCompra(Long id);
}
