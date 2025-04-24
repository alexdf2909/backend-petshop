package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.entidades.MarcaDTO;
import com.grupo8.petshop.entity.Marca;
import com.grupo8.petshop.service.IMarcaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/marca")
public class MarcaController {
    private final IMarcaService marcaService;

    public MarcaController(IMarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @PostMapping
    public ResponseEntity<Marca> crearMarca(@RequestBody MarcaDTO marcaDTO) {
        Marca marcaARetornar = marcaService.createMarca(marcaDTO);
        if (marcaARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(marcaARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<Marca>> traerTodos() {
        return ResponseEntity.ok(marcaService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Marca> buscarMarcaPorId(@PathVariable Long id) {
        Optional<Marca> marca = marcaService.searchForId(id);
        if (marca.isPresent()) {
            return ResponseEntity.ok(marca.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarMarca(@PathVariable Long id, @RequestBody MarcaDTO marcaDTO) {
        try {
            marcaService.updateMarca(id, marcaDTO);
            return ResponseEntity.ok("{\"message\": \"Marca modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarMarca(@PathVariable Long id) {
        Optional<Marca> marcaOptional = marcaService.searchForId(id);
        if (marcaOptional.isPresent()) {
            marcaService.deleteMarca(id);
            return ResponseEntity.ok("{\"message\": \"marca eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"marca no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}
