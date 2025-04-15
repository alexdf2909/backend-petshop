package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.PesoDTO;
import com.grupo8.petshop.entity.Peso;

import java.util.List;
import java.util.Optional;

public interface IPesoService {
    Peso createPeso(PesoDTO pesoDTO);
    Optional<Peso> searchForId(Long id);
    List<Peso> searchAll();
    void updatePeso(Long id, PesoDTO pesoDTO);
    void deletePeso(Long id);
}
