package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.entidades.PesoDTO;
import com.grupo8.petshop.entity.Peso;
import com.grupo8.petshop.entity.Variante;
import com.grupo8.petshop.repository.IPesoRepository;
import com.grupo8.petshop.repository.IVarianteRepository;
import com.grupo8.petshop.service.IPesoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PesoService implements IPesoService {
    private final IPesoRepository pesoRepository;
    @Autowired
    private IVarianteRepository varianteRepository;

    public PesoService(IPesoRepository pesoRepository) {
        this.pesoRepository = pesoRepository;
    }

    @Override
    public Peso createPeso(PesoDTO pesoDTO) {
        Peso peso = new Peso();
        peso.setValor(pesoDTO.getValor());

        return pesoRepository.save(peso);
    }

    @Override
    public Optional<Peso> searchForId(Long id) {
        return pesoRepository.findById(id);
    }

    @Override
    public List<Peso> searchAll() {
        return pesoRepository.findAll();
    }

    @Override
    public void updatePeso(Long id, PesoDTO pesoDTO) {
        Optional<Peso> pesoOpt = pesoRepository.findById(id);
        if (pesoOpt.isEmpty()) {
            throw new RuntimeException("Peso no encontrado");
        }

        Peso peso = pesoOpt.get();

        if (pesoDTO.getValor() != null && !pesoDTO.getValor().isEmpty()) {
            peso.setValor(pesoDTO.getValor());
        }

        pesoRepository.save(peso);
    }

    @Override
    public void deletePeso(Long id) {
        // Buscar la categoría a eliminar
        Optional<Peso> pesoOpt = pesoRepository.findById(id);
        if (pesoOpt.isEmpty()) {
            throw new RuntimeException("Peso no encontrado");
        }
        Peso peso = pesoOpt.get();
        // Buscar todas las prendas asociadas con esta categoría
        List<Variante> variantesWithPeso = varianteRepository.findByPeso(peso);
        // Pesor todas las prendas asociadas como eliminadas lógicamente
        for (Variante variante : variantesWithPeso) {
            variante.setPeso(null);
            variante.setDeleted(true);
        }
        // Guardar los cambios en las prendas
        varianteRepository.saveAll(variantesWithPeso);
        // Eliminar la categoría
        pesoRepository.delete(peso);
    }

    private PesoDTO convertToDTO(Peso peso) {
        return new PesoDTO(
                peso.getPesoId(),
                peso.getValor()
        );
    }
}
