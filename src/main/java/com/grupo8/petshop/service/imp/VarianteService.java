package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.VarianteDTO;
import com.grupo8.petshop.entity.*;
import com.grupo8.petshop.repository.*;
import com.grupo8.petshop.service.IVarianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VarianteService implements IVarianteService {
    private final IVarianteRepository varianteRepository;
    @Autowired
    private IProductoRepository productoRepository;
    @Autowired
    private ILoteRepository loteRepository;
    @Autowired
    private IColorRepository colorRepository;
    @Autowired
    private ITallaRepository tallaRepository;
    @Autowired
    private IPesoRepository pesoRepository;

    public VarianteService(IVarianteRepository varianteRepository) {
        this.varianteRepository = varianteRepository;
    }

    @Override
    public Variante createVariante(VarianteDTO varianteDTO) {
        Color color = colorRepository.findById(varianteDTO.getColorId())
                .orElseThrow(() -> new RuntimeException("Color no encontrada"));
        Talla talla = tallaRepository.findById(varianteDTO.getTallaId())
                .orElseThrow(() -> new RuntimeException("Talla no encontrada"));
        Peso peso = pesoRepository.findById(varianteDTO.getPesoId())
                .orElseThrow(() -> new RuntimeException("Peso no encontrada"));
        Producto producto = productoRepository.findById(varianteDTO.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrada"));

        Variante variante = new Variante();
        variante.setDeleted(false);
        variante.setPeso(peso);
        variante.setColor(color);
        variante.setTalla(talla);
        variante.setProducto(producto);
        variante.setPrecioOferta(varianteDTO.getPrecioOferta());
        variante.setPrecioOriginal(varianteDTO.getPrecioOriginal());

        // Agregar imágenes
        if (varianteDTO.getImagenes() != null) {
            varianteDTO.getImagenes().forEach(imageUrl -> {
                Imagen image = new Imagen();
                image.setImagenUrl(imageUrl);
                variante.getImagenes().add(image);
            });
        }

        return varianteRepository.save(variante);
    }

    @Override
    public Optional<Variante> searchForId(Long id) {
        return varianteRepository.findById(id);
    }

    @Override
    public List<Variante> searchAll() {
        return varianteRepository.findAll();
    }

    @Override
    public void updateVariante(Long id, VarianteDTO varianteDTO) {
        Optional<Variante> varianteOpt = varianteRepository.findById(id);
        if (varianteOpt.isEmpty()) {
            throw new RuntimeException("Variante no encontrada");
        }

        Variante variante = varianteOpt.get();

        if (varianteDTO.getProductoId() != null) {
            Producto producto = productoRepository.findById(varianteDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrada"));
            variante.setProducto(producto);
        }
        if (varianteDTO.getPesoId() != null) {
            Peso peso = pesoRepository.findById(varianteDTO.getPesoId())
                    .orElseThrow(() -> new RuntimeException("Peso no encontrada"));
            variante.setPeso(peso);
        }
        if (varianteDTO.getTallaId() != null) {
            Talla talla = tallaRepository.findById(varianteDTO.getTallaId())
                    .orElseThrow(() -> new RuntimeException("Talla no encontrada"));
            variante.setTalla(talla);
        }
        if (varianteDTO.getColorId() != null) {
            Color color = colorRepository.findById(varianteDTO.getColorId())
                    .orElseThrow(() -> new RuntimeException("Color no encontrada"));
            variante.setColor(color);
        }
        if (varianteDTO.getPrecioOferta() > 0.0) {
            variante.setPrecioOferta(varianteDTO.getPrecioOferta());
        }
        if (varianteDTO.getPrecioOriginal() > 0.0) {
            variante.setPrecioOriginal(varianteDTO.getPrecioOriginal());
        }
        if(varianteDTO.isDeleted() && !variante.isDeleted()){
            deleteVariante(varianteDTO.getVarianteId());
        }

        if (varianteDTO.getImagenes() != null) {
            variante.getImagenes().clear();
            varianteDTO.getImagenes().forEach(imageUrl -> {
                Imagen image = new Imagen();
                image.setImagenUrl(imageUrl);
                variante.getImagenes().add(image);
            });
        }

        varianteRepository.save(variante);
    }

    @Override
    public void deleteVariante(Long id) {
        // Buscar la categoría a eliminar
        Optional<Variante> varianteOpt = varianteRepository.findById(id);
        if (varianteOpt.isEmpty()) {
            throw new RuntimeException("Categoría no encontrada");
        }
        Variante variante = varianteOpt.get();
        // Buscar todas las prendas asociadas con esta categoría
        List<Lote> lotesWithVariante = loteRepository.findByVariante(variante);
        // Varianter todas las prendas asociadas como eliminadas lógicamente
        for (Lote lote : lotesWithVariante) {
            lote.setDeleted(true);
        }
        // Guardar los cambios en las prendas
        loteRepository.saveAll(lotesWithVariante);
        // Eliminar la categoría
        varianteRepository.delete(variante);
    }

    private VarianteDTO convertToDTO(Variante variante) {
        return new VarianteDTO(
                variante.getVarianteId(),
                variante.getProducto().getProductoId(),
                variante.getColor().getColorId(),
                variante.getTalla().getTallaId(),
                variante.getPeso().getPesoId(),
                variante.getPrecioOriginal(),
                variante.getPrecioOferta(),
                variante.getImagenes().stream()
                        .map(Imagen::getImagenUrl)
                        .collect(Collectors.toSet()),
                variante.isDeleted()
        );
    }
}
