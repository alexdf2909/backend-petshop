package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.entidades.PesoDTO;
import com.grupo8.petshop.entity.Peso;
import com.grupo8.petshop.service.IPesoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/peso")
public class PesoController {
    private final IPesoService pesoService;

    public PesoController(IPesoService pesoService) {
        this.pesoService = pesoService;
    }

    @PostMapping
    public ResponseEntity<Peso> crearPeso(@RequestBody PesoDTO pesoDTO) {
        Peso pesoARetornar = pesoService.createPeso(pesoDTO);
        if (pesoARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(pesoARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<Peso>> traerTodos() {
        return ResponseEntity.ok(pesoService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Peso> buscarPesoPorId(@PathVariable Long id) {
        Optional<Peso> peso = pesoService.searchForId(id);
        if (peso.isPresent()) {
            return ResponseEntity.ok(peso.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarPeso(@PathVariable Long id, @RequestBody PesoDTO pesoDTO) {
        try {
            pesoService.updatePeso(id, pesoDTO);
            return ResponseEntity.ok("{\"message\": \"Peso modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarPeso(@PathVariable Long id) {
        Optional<Peso> pesoOptional = pesoService.searchForId(id);
        if (pesoOptional.isPresent()) {
            pesoService.deletePeso(id);
            return ResponseEntity.ok("{\"message\": \"peso eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"peso no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}