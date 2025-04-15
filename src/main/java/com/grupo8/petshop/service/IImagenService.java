package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.ImagenDTO;

import java.util.List;
import java.util.Optional;

public interface IImagenService {
    ImagenDTO createImagen(ImagenDTO imagenDTO);
    Optional<ImagenDTO> searchForId(Long id);
    List<ImagenDTO> searchAll();
    void updateImagen(Long id, ImagenDTO imagenDTO);
    void deleteImagen(Long id);
}
