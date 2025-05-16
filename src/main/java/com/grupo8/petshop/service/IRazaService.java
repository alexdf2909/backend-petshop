package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.RazaDTO;
import com.grupo8.petshop.entity.Raza;

import java.util.List;
import java.util.Optional;

public interface IRazaService {
    Raza createRaza(RazaDTO razaDTO);
    Optional<Raza> searchForId(Long id);
    List<Raza> searchAll();
    void updateRaza(Long id, RazaDTO razaDTO);
    void deleteRaza(Long id);
}
