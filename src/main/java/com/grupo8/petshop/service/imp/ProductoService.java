package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.ProductoDTO;
import com.grupo8.petshop.entity.*;
import com.grupo8.petshop.repository.*;
import com.grupo8.petshop.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductoService implements IProductoService {
    private final IProductoRepository productoRepository;
    @Autowired
    private IVarianteRepository varianteRepository;
    @Autowired
    private ICategoriaRepository categoriaRepository;
    @Autowired
    private IMarcaRepository marcaRepository;
    @Autowired
    private IEspecieRepository especieRepository;
    @Autowired
    private IEtiquetaRepository etiquetaRepository;
    @Autowired
    private ILoteRepository loteRepository;

    public ProductoService(IProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public ProductoDTO createProducto(ProductoDTO productoDTO) {
        Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        Marca marca = marcaRepository.findById(productoDTO.getMarcaId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        Especie especie = especieRepository.findById(productoDTO.getEspecieId())
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));

        Producto producto = new Producto();
        producto.setDeleted(productoDTO.isDeleted());
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setEspecie(especie);

        if (productoDTO.getEtiquetaIds() != null) {
            productoDTO.getEtiquetaIds().forEach(etiquetaId -> {
                Optional<Etiqueta> etiquetaOpt = etiquetaRepository.findById(etiquetaId);
                etiquetaOpt.ifPresent(producto.getEtiquetas()::add);
            });
        }

        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());

        Producto savedProducto = productoRepository.save(producto);

        return convertToDTO(savedProducto);
    }

    @Override
    public Optional<ProductoDTO> searchForId(Long id) {
        return productoRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<ProductoDTO> searchAll() {
        return productoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateProducto(Long id, ProductoDTO productoDTO) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("Producto no encontrado");
        }

        Producto producto = productoOpt.get();

        if (productoDTO.getNombre() != null && !productoDTO.getNombre().isEmpty()) {
            producto.setNombre(productoDTO.getNombre());
        }
        if (productoDTO.getDescripcion() != null && !productoDTO.getDescripcion().isEmpty()) {
            producto.setDescripcion(productoDTO.getDescripcion());
        }
        if (productoDTO.getMarcaId() != null) {
            Marca marca = marcaRepository.findById(productoDTO.getMarcaId())
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
            producto.setMarca(marca);
        }
        if (productoDTO.getEspecieId() != null) {
            Especie especie = especieRepository.findById(productoDTO.getEspecieId())
                    .orElseThrow(() -> new RuntimeException("Especie no encontrada"));
            producto.setEspecie(especie);
        }
        if (productoDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
            producto.setCategoria(categoria);
        }
        if(productoDTO.isDeleted() && !producto.isDeleted()){
            deleteProducto(productoDTO.getProductoId());
        }

        if (productoDTO.getEtiquetaIds() != null) {
            Set<Etiqueta> updatedEtiquetas = new HashSet<>();
            productoDTO.getEtiquetaIds().forEach(etiquetaId -> {
                etiquetaRepository.findById(etiquetaId).ifPresent(updatedEtiquetas::add);
            });
            producto.setEtiquetas(updatedEtiquetas);
        }

        productoRepository.save(producto);
    }

    @Override
    public void deleteProducto(Long id) {
        // Buscar la categoría a eliminar
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("Producto no encontrada");
        }
        Producto producto = productoOpt.get();
        // Buscar todas las prendas asociadas con esta categoría
        List<Variante> variantesWithProducto = varianteRepository.findByProducto(producto);
        // Productor todas las prendas asociadas como eliminadas lógicamente
        for (Variante variante : variantesWithProducto) {
            List<Lote> lotesWithVariante = loteRepository.findByVariante(variante);
            variante.setDeleted(true);
            for (Lote lote : lotesWithVariante){
                lote.setDeleted(true);
            }
            loteRepository.saveAll(lotesWithVariante);
        }
        // Guardar los cambios en las prendas
        varianteRepository.saveAll(variantesWithProducto);
        // Eliminar la categoría
        productoRepository.delete(producto);
    }

    private ProductoDTO convertToDTO(Producto producto) {
        return new ProductoDTO(
                producto.getProductoId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getEspecie().getEspecieId(),
                producto.getMarca().getMarcaId(),
                producto.getCategoria().getCategoriaId(),
                producto.getEtiquetas().stream().map(Etiqueta::getEtiquetaId).collect(Collectors.toSet()),
                producto.isDeleted()
        );
    }
}

