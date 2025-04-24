package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.entidades.VarianteDTO;
import com.grupo8.petshop.entity.Variante;
import com.grupo8.petshop.service.IVarianteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/variante")
public class VarianteController {
    private final IVarianteService varianteService;

    public VarianteController(IVarianteService varianteService) {
        this.varianteService = varianteService;
    }

    @PostMapping
    public ResponseEntity<Variante> crearVariante(@RequestBody VarianteDTO varianteDTO) {
        Variante varianteARetornar = varianteService.createVariante(varianteDTO);
        if (varianteARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(varianteARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<Variante>> traerTodos() {
        return ResponseEntity.ok(varianteService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Variante> buscarVariantePorId(@PathVariable Long id) {
        Optional<Variante> variante = varianteService.searchForId(id);
        if (variante.isPresent()) {
            return ResponseEntity.ok(variante.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Variante>> getVariantesByProducto(@PathVariable Long productoId) {
        List<Variante> variantes = varianteService.searchByProducto(productoId);
        return ResponseEntity.ok(variantes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarVariante(@PathVariable Long id, @RequestBody VarianteDTO varianteDTO) {
        try {
            varianteService.updateVariante(id, varianteDTO);
            return ResponseEntity.ok("{\"message\": \"Variante modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarVariante(@PathVariable Long id) {
        Optional<Variante> varianteOptional = varianteService.searchForId(id);
        if (varianteOptional.isPresent()) {
            varianteService.deleteVariante(id);
            return ResponseEntity.ok("{\"message\": \"variante eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"variante no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}