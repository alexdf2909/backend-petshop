package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.CategoriaDTO;
import com.grupo8.petshop.entity.Categoria;
import com.grupo8.petshop.service.ICategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {
    private final ICategoriaService categoriaService; //comentario

    public CategoriaController(ICategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        Categoria categoriaARetornar = categoriaService.createCategoria(categoriaDTO);
        if (categoriaARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> traerTodos() {
        return ResponseEntity.ok(categoriaService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarCategoriaPorId(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaService.searchForId(id);
        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO) {
        try {
            categoriaService.updateCategoria(id, categoriaDTO);
            return ResponseEntity.ok("{\"message\": \"Categoria modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarCategoria(@PathVariable Long id) {
        Optional<Categoria> categoriaOptional = categoriaService.searchForId(id);
        if (categoriaOptional.isPresent()) {
            categoriaService.deleteCategoria(id);
            return ResponseEntity.ok("{\"message\": \"categoria eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"categoria no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}
