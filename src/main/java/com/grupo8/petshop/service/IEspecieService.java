package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.EspecieDTO;
import com.grupo8.petshop.entity.Especie;

import java.util.List;
import java.util.Optional;

public interface IEspecieService {
    Especie createEspecie(EspecieDTO especieDTO);
    Optional<Especie> searchForId(Long id);
    List<Especie> searchAll();
    void updateEspecie(Long id, EspecieDTO especieDTO);
    void deleteEspecie(Long id);
}
