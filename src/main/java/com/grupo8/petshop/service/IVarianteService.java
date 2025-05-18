package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.VarianteConStockDTO;
import com.grupo8.petshop.dto.entidades.VarianteDTO;
import com.grupo8.petshop.entity.Lote;
import com.grupo8.petshop.entity.Variante;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface IVarianteService {
    Variante createVariante(VarianteDTO varianteDTO);
    Optional<Variante> searchForId(Long id);
    List<Variante> searchAll();
    void updateVariante(Long id, VarianteDTO varianteDTO);
    void deleteVariante(Long id);
    List<Variante> searchByProducto(Long productoId);
    List<VarianteConStockDTO> obtenerVariantesConBajoStock();
}