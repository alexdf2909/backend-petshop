package com.grupo8.petshop.service;


import com.grupo8.petshop.dto.CategoriaDTO;
import com.grupo8.petshop.entity.Categoria;

import java.util.List;
import java.util.Optional;

public interface ICategoriaService {
    Categoria createCategoria(CategoriaDTO categoriaDTO);
    Optional<Categoria> searchForId(Long id);
    List<Categoria> searchAll();
    void updateCategoria(Long id, CategoriaDTO categoriaDTO);
    void deleteCategoria(Long id);
}
