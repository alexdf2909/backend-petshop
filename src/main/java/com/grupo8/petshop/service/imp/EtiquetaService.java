package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.EtiquetaDTO;
import com.grupo8.petshop.entity.Etiqueta;
import com.grupo8.petshop.entity.Producto;
import com.grupo8.petshop.repository.IEtiquetaRepository;
import com.grupo8.petshop.repository.IProductoRepository;
import com.grupo8.petshop.service.IEtiquetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EtiquetaService implements IEtiquetaService {
    private final IEtiquetaRepository etiquetaRepository;

    @Autowired
    private IProductoRepository productoRepository;

    public EtiquetaService(IEtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    @Override
    public Etiqueta createEtiqueta(EtiquetaDTO etiquetaDTO) {
        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setNombre(etiquetaDTO.getNombre());

        return etiquetaRepository.save(etiqueta);
    }

    @Override
    public Optional<Etiqueta> searchForId(Long id) {
        Optional<Etiqueta> etiqueta = etiquetaRepository.findById(id);
        if (etiqueta.isPresent()) {
            Etiqueta savedEtiqueta = etiqueta.get();
            return Optional.of(savedEtiqueta);
        }
        return Optional.empty();
    }

    @Override
    public List<Etiqueta> searchAll() {
        return etiquetaRepository.findAll();
    }

    @Override
    public void updateEtiqueta(Long id, EtiquetaDTO etiquetaDTO) {
        Optional<Etiqueta> etiquetaOpt = etiquetaRepository.findById(id);
        if (etiquetaOpt.isPresent()) {
            Etiqueta existingEtiqueta = etiquetaOpt.get();
            if (etiquetaDTO.getNombre() != null && !etiquetaDTO.getNombre().isEmpty()) {
                existingEtiqueta.setNombre(etiquetaDTO.getNombre());
            }

            etiquetaRepository.save(existingEtiqueta);
        }
    }

    @Override
    public void deleteEtiqueta(Long id) {
        Optional<Etiqueta> etiquetaOpt = etiquetaRepository.findById(id);
        if (etiquetaOpt.isPresent()) {
            Etiqueta etiqueta = etiquetaOpt.get();
            // Buscar todas las prendas que contienen este atributo
            List<Producto> productosWithEtiqueta = productoRepository.findByEtiquetasContaining(etiqueta);
            // Marcar todas las prendas asociadas como eliminadas lógicamente
            for (Producto producto : productosWithEtiqueta) {
                producto.getEtiquetas().remove(etiqueta);
            }
            // Guardar los cambios en las prendas
            productoRepository.saveAll(productosWithEtiqueta);
            // Eliminar el atributo
            etiquetaRepository.delete(etiqueta);
        } else {
            throw new RuntimeException("Etiqueta no encontrada");
        }
    }

}
