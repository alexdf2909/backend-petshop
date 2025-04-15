package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.TallaDTO;
import com.grupo8.petshop.entity.Talla;
import com.grupo8.petshop.entity.Variante;
import com.grupo8.petshop.repository.ITallaRepository;
import com.grupo8.petshop.repository.IVarianteRepository;
import com.grupo8.petshop.service.ITallaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TallaService implements ITallaService {
    private final ITallaRepository tallaRepository;
    @Autowired
    private IVarianteRepository varianteRepository;

    public TallaService(ITallaRepository tallaRepository) {
        this.tallaRepository = tallaRepository;
    }

    @Override
    public Talla createTalla(TallaDTO tallaDTO) {
        Talla talla = new Talla();
        talla.setValor(tallaDTO.getValor());

        return tallaRepository.save(talla);

    }

    @Override
    public Optional<Talla> searchForId(Long id) {
        return tallaRepository.findById(id);
    }

    @Override
    public List<Talla> searchAll() {
        return tallaRepository.findAll();
    }

    @Override
    public void updateTalla(Long id, TallaDTO tallaDTO) {
        Optional<Talla> tallaOpt = tallaRepository.findById(id);
        if (tallaOpt.isEmpty()) {
            throw new RuntimeException("Talla no encontrada");
        }

        Talla talla = tallaOpt.get();

        if (tallaDTO.getValor() != null && !tallaDTO.getValor().isEmpty()) {
            talla.setValor(tallaDTO.getValor());
        }

        tallaRepository.save(talla);
    }

    @Override
    public void deleteTalla(Long id) {
        // Buscar la categoría a eliminar
        Optional<Talla> tallaOpt = tallaRepository.findById(id);
        if (tallaOpt.isEmpty()) {
            throw new RuntimeException("Talla no encontrada");
        }
        Talla talla = tallaOpt.get();
        // Buscar todas las prendas asociadas con esta categoría
        List<Variante> variantesWithTalla = varianteRepository.findByTalla(talla);
        // Tallar todas las prendas asociadas como eliminadas lógicamente
        for (Variante variante : variantesWithTalla) {
            variante.setTalla(null);
            variante.setDeleted(true);
        }
        // Guardar los cambios en las prendas
        varianteRepository.saveAll(variantesWithTalla);
        // Eliminar la categoría
        tallaRepository.delete(talla);
    }

    private TallaDTO convertToDTO(Talla talla) {
        return new TallaDTO(
                talla.getTallaId(),
                talla.getValor()
        );
    }
}
