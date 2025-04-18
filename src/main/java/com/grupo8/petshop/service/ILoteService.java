package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.LoteDTO;
import com.grupo8.petshop.entity.Lote;
import com.grupo8.petshop.entity.Variante;

import java.util.List;
import java.util.Optional;

public interface ILoteService {
    Lote createLote(LoteDTO loteDTO);
    Optional<Lote> searchForId(Long id);
    List<Lote> searchAll();
    void updateLote(Long id, LoteDTO loteDTO);
    void deleteLote(Long id);
    List<Lote> searchByVariante(Long varianteId);
    List<Lote> searchByCompra(Long compraId);
}
