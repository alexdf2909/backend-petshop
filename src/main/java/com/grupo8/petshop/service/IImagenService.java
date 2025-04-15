package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.ImagenDTO;
import com.grupo8.petshop.entity.Imagen;

import java.util.List;
import java.util.Optional;

public interface IImagenService {
    Imagen createImagen(ImagenDTO imagenDTO);
    Optional<Imagen> searchForId(Long id);
    List<Imagen> searchAll();
    void updateImagen(Long id, ImagenDTO imagenDTO);
    void deleteImagen(Long id);
}
