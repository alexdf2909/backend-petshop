package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.RazaDTO;
import com.grupo8.petshop.entity.*;
import com.grupo8.petshop.repository.*;
import com.grupo8.petshop.service.IRazaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RazaService implements IRazaService {
    private final IRazaRepository razaRepository;

    @Autowired
    private IEspecieRepository especieRepository;


    public RazaService(IRazaRepository razaRepository) {
        this.razaRepository = razaRepository;
    }

    @Override
    public Raza createRaza(RazaDTO razaDTO) {




        Raza raza = new Raza();
        raza.setNombre(razaDTO.getNombre());
        if (razaDTO.getEspecieId() != null) {
            Especie especie = especieRepository.findById(razaDTO.getEspecieId())
                    .orElseThrow(() -> new RuntimeException("Especie no encontrada"));
            raza.setEspecie(especie);
        }

        return razaRepository.save(raza);

    }

    @Override
    public Optional<Raza> searchForId(Long id) {
        return razaRepository.findById(id);
    }

    @Override
    public List<Raza> searchAll() {
        return razaRepository.findAll();
    }

    @Override
    public void updateRaza(Long id, RazaDTO razaDTO) {
        Optional<Raza> razaOpt = razaRepository.findById(id);
        if (razaOpt.isEmpty()) {
            throw new RuntimeException("Raza no encontrada");
        }

        Raza raza = razaOpt.get();

        if (razaDTO.getEspecieId() != null) {
            Especie especie = especieRepository.findById(razaDTO.getEspecieId())
                    .orElseThrow(() -> new RuntimeException("Especie no encontrada"));
            raza.setEspecie(especie);
        }else{
            raza.setEspecie(null);
        }

        if (razaDTO.getNombre() != null && !razaDTO.getNombre().isEmpty()) {
            raza.setNombre(razaDTO.getNombre());
        }

        razaRepository.save(raza);
    }

    @Override
    public void deleteRaza(Long id) {
        // Buscar la categoría a eliminar
        Optional<Raza> razaOpt = razaRepository.findById(id);
        if (razaOpt.isEmpty()) {
            throw new RuntimeException("Raza no encontrada");
        }
        Raza raza = razaOpt.get();

        // Eliminar la categoría
        //razaRepository.delete(raza); no se va eliminar ya que Mascota lo tiene
    }

    private RazaDTO convertToDTO(Raza raza) {
        return new RazaDTO(
                raza.getRazaId(),
                raza.getNombre(),
                raza.getEspecie().getEspecieId()
        );
    }
}