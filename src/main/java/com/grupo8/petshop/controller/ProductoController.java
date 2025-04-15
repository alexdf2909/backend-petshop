package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.ProductoDTO;
import com.grupo8.petshop.service.IProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/producto")
public class ProductoController {
    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@RequestBody ProductoDTO productoDTO) {
        ProductoDTO productoARetornar = productoService.createProducto(productoDTO);
        if (productoARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(productoARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> traerTodos() {
        return ResponseEntity.ok(productoService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> buscarProductoPorId(@PathVariable Long id) {
        Optional<ProductoDTO> producto = productoService.searchForId(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        try {
            productoService.updateProducto(id, productoDTO);
            return ResponseEntity.ok("{\"message\": \"Producto modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarProducto(@PathVariable Long id) {
        Optional<ProductoDTO> productoOptional = productoService.searchForId(id);
        if (productoOptional.isPresent()) {
            productoService.deleteProducto(id);
            return ResponseEntity.ok("{\"message\": \"producto eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"producto no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}