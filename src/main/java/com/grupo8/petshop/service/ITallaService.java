package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.TallaDTO;

import java.util.List;
import java.util.Optional;

public interface ITallaService {
    TallaDTO createTalla(TallaDTO tallaDTO);
    Optional<TallaDTO> searchForId(Long id);
    List<TallaDTO> searchAll();
    void updateTalla(Long id, TallaDTO tallaDTO);
    void deleteTalla(Long id);
}
