package com.grupo8.petshop.service;


import com.grupo8.petshop.dto.CategoriaDTO;

import java.util.List;
import java.util.Optional;

public interface ICategoriaService {
    CategoriaDTO createCategoria(CategoriaDTO categoriaDTO);
    Optional<CategoriaDTO> searchForId(Long id);
    List<CategoriaDTO> searchAll();
    void updateCategoria(Long id, CategoriaDTO categoriaDTO);
    void deleteCategoria(Long id);
}
