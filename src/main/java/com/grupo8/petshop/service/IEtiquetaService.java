package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.EtiquetaDTO;

import java.util.List;
import java.util.Optional;

public interface IEtiquetaService {
    EtiquetaDTO createEtiqueta(EtiquetaDTO etiquetaDTO);
    Optional<EtiquetaDTO> searchForId(Long id);
    List<EtiquetaDTO> searchAll();
    void updateEtiqueta(Long id, EtiquetaDTO etiquetaDTO);
    void deleteEtiqueta(Long id);
}
