package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.ProductoDTO;
import com.grupo8.petshop.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {
    Producto createProducto(ProductoDTO productoDTO);
    Optional<Producto> searchForId(Long id);
    List<Producto> searchAll();
    void updateProducto(Long id, ProductoDTO productoDTO);
    void deleteProducto(Long id);
    List<Producto> searchNombre(String name);
}
