package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.EtiquetaDTO;
import com.grupo8.petshop.entity.Etiqueta;

import java.util.List;
import java.util.Optional;

public interface IEtiquetaService {
    Etiqueta createEtiqueta(EtiquetaDTO etiquetaDTO);
    Optional<Etiqueta> searchForId(Long id);
    List<Etiqueta> searchAll();
    void updateEtiqueta(Long id, EtiquetaDTO etiquetaDTO);
    void deleteEtiqueta(Long id);
}
