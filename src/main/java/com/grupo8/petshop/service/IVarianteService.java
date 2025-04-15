package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.VarianteDTO;

import java.util.List;
import java.util.Optional;

public interface IVarianteService {
    VarianteDTO createVariante(VarianteDTO varianteDTO);
    Optional<VarianteDTO> searchForId(Long id);
    List<VarianteDTO> searchAll();
    void updateVariante(Long id, VarianteDTO varianteDTO);
    void deleteVariante(Long id);
}