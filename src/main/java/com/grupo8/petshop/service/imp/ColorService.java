package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.entidades.ColorDTO;
import com.grupo8.petshop.entity.Color;
import com.grupo8.petshop.entity.Variante;
import com.grupo8.petshop.repository.IColorRepository;
import com.grupo8.petshop.repository.IVarianteRepository;
import com.grupo8.petshop.service.IColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorService implements IColorService {
    private final IColorRepository colorRepository;
    @Autowired
    private IVarianteRepository varianteRepository;

    public ColorService(IColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Override
    public Color createColor(ColorDTO colorDTO) {
        Color color = new Color();
        color.setValor(colorDTO.getValor());

        return colorRepository.save(color);
    }

    @Override
    public Optional<Color> searchForId(Long id) {
        return colorRepository.findById(id);
    }

    @Override
    public List<Color> searchAll() {
        return colorRepository.findAll();
    }

    @Override
    public void updateColor(Long id, ColorDTO colorDTO) {
        Optional<Color> colorOpt = colorRepository.findById(id);
        if (colorOpt.isEmpty()) {
            throw new RuntimeException("Color no encontrado");
        }

        Color color = colorOpt.get();

        if (colorDTO.getValor() != null && !colorDTO.getValor().isEmpty()) {
            color.setValor(colorDTO.getValor());
        }

        colorRepository.save(color);
    }

    @Override
    public void deleteColor(Long id) {
        // Buscar la categoría a eliminar
        Optional<Color> colorOpt = colorRepository.findById(id);
        if (colorOpt.isEmpty()) {
            throw new RuntimeException("Color no encontrado");
        }
        Color color = colorOpt.get();
        // Buscar todas los variantes asociadas con esta categoría
        List<Variante> variantesWithColor = varianteRepository.findByColor(color);
        // Marcar todas las prendas asociadas como eliminadas lógicamente
        for (Variante variante : variantesWithColor) {
            variante.setColor(null);
            variante.setDeleted(true);
        }
        // Guardar los cambios en las prendas
        varianteRepository.saveAll(variantesWithColor);
        // Eliminar la categoría
        colorRepository.delete(color);
    }

    private ColorDTO convertToDTO(Color color) {
        return new ColorDTO(
                color.getColorId(),
                color.getValor()
        );
    }
}