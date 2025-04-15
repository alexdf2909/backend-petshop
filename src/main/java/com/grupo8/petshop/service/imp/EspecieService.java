package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.EspecieDTO;
import com.grupo8.petshop.entity.Especie;
import com.grupo8.petshop.entity.Producto;
import com.grupo8.petshop.repository.IEspecieRepository;
import com.grupo8.petshop.repository.IProductoRepository;
import com.grupo8.petshop.service.IEspecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EspecieService implements IEspecieService {
    private final IEspecieRepository especieRepository;
    @Autowired
    private IProductoRepository productoRepository;

    public EspecieService(IEspecieRepository especieRepository) {
        this.especieRepository = especieRepository;
    }

    @Override
    public Especie createEspecie(EspecieDTO especieDTO) {
        Especie especie = new Especie();
        especie.setNombre(especieDTO.getNombre());
        especie.setImagenUrl(especieDTO.getImagenUrl());

        return especieRepository.save(especie);
    }

    @Override
    public Optional<Especie> searchForId(Long id) {
        return especieRepository.findById(id);
    }

    @Override
    public List<Especie> searchAll() {
        return especieRepository.findAll();
    }

    @Override
    public void updateEspecie(Long id, EspecieDTO especieDTO) {
        Optional<Especie> especieOpt = especieRepository.findById(id);
        if (especieOpt.isEmpty()) {
            throw new RuntimeException("Especie no encontrada");
        }

        Especie especie = especieOpt.get();

        if (especieDTO.getNombre() != null && !especieDTO.getNombre().isEmpty()) {
            especie.setNombre(especieDTO.getNombre());
        }
        if (especieDTO.getImagenUrl() != null && !especieDTO.getImagenUrl().isEmpty()) {
            especie.setImagenUrl(especieDTO.getImagenUrl());
        }

        especieRepository.save(especie);
    }

    @Override
    public void deleteEspecie(Long id) {
        // Buscar la categoría a eliminar
        Optional<Especie> especieOpt = especieRepository.findById(id);
        if (especieOpt.isEmpty()) {
            throw new RuntimeException("Especie no encontrada");
        }
        Especie especie = especieOpt.get();
        // Buscar todas las prendas asociadas con esta categoría
        List<Producto> productosWithEspecie = productoRepository.findByEspecie(especie);
        // Marcar todas las prendas asociadas como eliminadas lógicamente
        for (Producto clothe : productosWithEspecie) {
            clothe.setEspecie(null);
            clothe.setDeleted(true);
        }
        // Guardar los cambios en las prendas
        productoRepository.saveAll(productosWithEspecie);
        // Eliminar la categoría
        especieRepository.delete(especie);
    }

    private EspecieDTO convertToDTO(Especie especie) {
        return new EspecieDTO(
                especie.getEspecieId(),
                especie.getNombre(),
                especie.getImagenUrl()
        );
    }
}
