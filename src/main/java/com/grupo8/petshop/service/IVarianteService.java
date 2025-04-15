package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.VarianteDTO;
import com.grupo8.petshop.entity.Variante;

import java.util.List;
import java.util.Optional;

public interface IVarianteService {
    Variante createVariante(VarianteDTO varianteDTO);
    Optional<Variante> searchForId(Long id);
    List<Variante> searchAll();
    void updateVariante(Long id, VarianteDTO varianteDTO);
    void deleteVariante(Long id);
}