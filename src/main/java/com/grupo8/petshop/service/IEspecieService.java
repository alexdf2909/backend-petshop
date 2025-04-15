package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.EspecieDTO;

import java.util.List;
import java.util.Optional;

public interface IEspecieService {
    EspecieDTO createEspecie(EspecieDTO especieDTO);
    Optional<EspecieDTO> searchForId(Long id);
    List<EspecieDTO> searchAll();
    void updateEspecie(Long id, EspecieDTO especieDTO);
    void deleteEspecie(Long id);
}
