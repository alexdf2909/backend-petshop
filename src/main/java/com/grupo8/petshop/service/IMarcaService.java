package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.MarcaDTO;

import java.util.List;
import java.util.Optional;

public interface IMarcaService {
    MarcaDTO createMarca(MarcaDTO marcaDTO);
    Optional<MarcaDTO> searchForId(Long id);
    List<MarcaDTO> searchAll();
    void updateMarca(Long id, MarcaDTO marcaDTO);
    void deleteMarca(Long id);
}
