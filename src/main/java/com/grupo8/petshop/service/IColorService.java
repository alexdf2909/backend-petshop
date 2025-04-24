package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.entidades.ColorDTO;
import com.grupo8.petshop.entity.Color;

import java.util.List;
import java.util.Optional;

public interface IColorService {
    Color createColor(ColorDTO colorDTO);
    Optional<Color> searchForId(Long id);
    List<Color> searchAll();
    void updateColor(Long id, ColorDTO colorDTO);
    void deleteColor(Long id);
}
