package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.CategoriaDTO;
import com.grupo8.petshop.entity.Categoria;
import com.grupo8.petshop.entity.Producto;
import com.grupo8.petshop.repository.ICategoriaRepository;
import com.grupo8.petshop.repository.IProductoRepository;
import com.grupo8.petshop.service.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService implements ICategoriaService {
    private final ICategoriaRepository categoriaRepository;
    @Autowired
    private IProductoRepository productoRepository;

    public CategoriaService(ICategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public Categoria createCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setImagenUrl(categoriaDTO.getImagenUrl());

        return categoriaRepository.save(categoria);
    }

    @Override
    public Optional<Categoria> searchForId(Long id) {
        return categoriaRepository.findById(id);
    }

    @Override
    public List<Categoria> searchAll() {
        return categoriaRepository.findAll();
    }

    @Override
    public void updateCategoria(Long id, CategoriaDTO categoriaDTO) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        if (categoriaOpt.isEmpty()) {
            throw new RuntimeException("Categoría no encontrada");
        }

        Categoria categoria = categoriaOpt.get();

        if (categoriaDTO.getNombre() != null && !categoriaDTO.getNombre().isEmpty()) {
            categoria.setNombre(categoriaDTO.getNombre());
        }
        if (categoriaDTO.getImagenUrl() != null && !categoriaDTO.getImagenUrl().isEmpty()) {
            categoria.setImagenUrl(categoriaDTO.getImagenUrl());
        }

        categoriaRepository.save(categoria);
    }

    @Override
    public void deleteCategoria(Long id) {
        // Buscar la categoría a eliminar
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        if (categoriaOpt.isEmpty()) {
            throw new RuntimeException("Categoría no encontrada");
        }
        Categoria categoria = categoriaOpt.get();

        // Buscar todos los productos asociadas con esta categoría
        List<Producto> productosWithCategoria = productoRepository.findByCategoria(categoria);

        // Marcar todas las productos asociadas como eliminadas lógicamente
        for (Producto producto : productosWithCategoria) {
            producto.setCategoria(null);
            producto.setDeleted(true);
        }
        // Guardar los cambios en las productos
        productoRepository.saveAll(productosWithCategoria);

        // Eliminar la categoría
        categoriaRepository.delete(categoria);
    }

    private CategoriaDTO convertToDTO(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getCategoriaId(),
                categoria.getNombre(),
                categoria.getImagenUrl()
        );
    }
}