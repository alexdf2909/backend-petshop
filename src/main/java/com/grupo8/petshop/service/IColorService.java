package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.ColorDTO;

import java.util.List;
import java.util.Optional;

public interface IColorService {
    ColorDTO createColor(ColorDTO colorDTO);
    Optional<ColorDTO> searchForId(Long id);
    List<ColorDTO> searchAll();
    void updateColor(Long id, ColorDTO colorDTO);
    void deleteColor(Long id);
}
