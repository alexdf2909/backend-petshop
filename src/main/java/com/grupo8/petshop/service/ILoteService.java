package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.LoteDTO;

import java.util.List;
import java.util.Optional;

public interface ILoteService {
    LoteDTO createLote(LoteDTO loteDTO);
    Optional<LoteDTO> searchForId(Long id);
    List<LoteDTO> searchAll();
    void updateLote(Long id, LoteDTO loteDTO);
    void deleteLote(Long id);
}
