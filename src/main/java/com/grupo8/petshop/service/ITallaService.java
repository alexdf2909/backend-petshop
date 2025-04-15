package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.TallaDTO;
import com.grupo8.petshop.entity.Talla;

import java.util.List;
import java.util.Optional;

public interface ITallaService {
    Talla createTalla(TallaDTO tallaDTO);
    Optional<Talla> searchForId(Long id);
    List<Talla> searchAll();
    void updateTalla(Long id, TallaDTO tallaDTO);
    void deleteTalla(Long id);
}
