package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.CompraDTO;

import java.util.List;
import java.util.Optional;

public interface ICompraService {
    CompraDTO createCompra(CompraDTO compraDTO);
    Optional<CompraDTO> searchForId(Long id);
    List<CompraDTO> searchAll();
    void updateCompra(Long id, CompraDTO compraDTO);
    void deleteCompra(Long id);
}
