package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.MarcaDTO;
import com.grupo8.petshop.entity.Marca;
import com.grupo8.petshop.entity.Producto;
import com.grupo8.petshop.repository.IMarcaRepository;
import com.grupo8.petshop.repository.IProductoRepository;
import com.grupo8.petshop.service.IMarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarcaService implements IMarcaService {
    private final IMarcaRepository marcaRepository;
    @Autowired
    private IProductoRepository productoRepository;

    public MarcaService(IMarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @Override
    public Marca createMarca(MarcaDTO marcaDTO) {
        Marca marca = new Marca();
        marca.setNombre(marcaDTO.getNombre());
        marca.setImagenUrl(marcaDTO.getImagenUrl());

        return marcaRepository.save(marca);
    }

    @Override
    public Optional<Marca> searchForId(Long id) {
        return marcaRepository.findById(id);
    }

    @Override
    public List<Marca> searchAll() {
        return marcaRepository.findAll();
    }

    @Override
    public void updateMarca(Long id, MarcaDTO marcaDTO) {
        Optional<Marca> marcaOpt = marcaRepository.findById(id);
        if (marcaOpt.isEmpty()) {
            throw new RuntimeException("Marca no encontrada");
        }

        Marca marca = marcaOpt.get();

        if (marcaDTO.getNombre() != null && !marcaDTO.getNombre().isEmpty()) {
            marca.setNombre(marcaDTO.getNombre());
        }
        if (marcaDTO.getImagenUrl() != null && !marcaDTO.getImagenUrl().isEmpty()) {
            marca.setImagenUrl(marcaDTO.getImagenUrl());
        }

        marcaRepository.save(marca);
    }

    @Override
    public void deleteMarca(Long id) {
        // Buscar la categoría a eliminar
        Optional<Marca> marcaOpt = marcaRepository.findById(id);
        if (marcaOpt.isEmpty()) {
            throw new RuntimeException("Marca no encontrada");
        }
        Marca marca = marcaOpt.get();
        // Buscar todas las prendas asociadas con esta categoría
        List<Producto> productosWithMarca = productoRepository.findByMarca(marca);
        // Marcar todas las prendas asociadas como eliminadas lógicamente
        for (Producto producto : productosWithMarca) {
            producto.setMarca(null);
            producto.setDeleted(true);
        }
        // Guardar los cambios en las prendas
        productoRepository.saveAll(productosWithMarca);
        // Eliminar la categoría
        marcaRepository.delete(marca);
    }

    private MarcaDTO convertToDTO(Marca marca) {
        return new MarcaDTO(
                marca.getMarcaId(),
                marca.getNombre(),
                marca.getImagenUrl()
        );
    }
}
