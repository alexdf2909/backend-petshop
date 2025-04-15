package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.MarcaDTO;
import com.grupo8.petshop.entity.Marca;

import java.util.List;
import java.util.Optional;

public interface IMarcaService {
    Marca createMarca(MarcaDTO marcaDTO);
    Optional<Marca> searchForId(Long id);
    List<Marca> searchAll();
    void updateMarca(Long id, MarcaDTO marcaDTO);
    void deleteMarca(Long id);
}
