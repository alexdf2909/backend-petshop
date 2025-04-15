package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.PesoDTO;

import java.util.List;
import java.util.Optional;

public interface IPesoService {
    PesoDTO createPeso(PesoDTO pesoDTO);
    Optional<PesoDTO> searchForId(Long id);
    List<PesoDTO> searchAll();
    void updatePeso(Long id, PesoDTO pesoDTO);
    void deletePeso(Long id);
}
