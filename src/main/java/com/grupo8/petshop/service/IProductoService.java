package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.ProductoDTO;

import java.util.List;
import java.util.Optional;

public interface IProductoService {
    ProductoDTO createProducto(ProductoDTO productoDTO);
    Optional<ProductoDTO> searchForId(Long id);
    List<ProductoDTO> searchAll();
    void updateProducto(Long id, ProductoDTO productoDTO);
    void deleteProducto(Long id);
}
