package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.RazaDTO;
import com.grupo8.petshop.entity.Raza;
import com.grupo8.petshop.service.IRazaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/raza")
public class RazaController {
    private final IRazaService razaService;

    public RazaController(IRazaService razaService) {
        this.razaService = razaService;
    }

    @PostMapping
    public ResponseEntity<Raza> crearRaza(@RequestBody RazaDTO razaDTO) {
        Raza razaARetornar = razaService.createRaza(razaDTO);
        if (razaARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(razaARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<Raza>> traerTodos() {
        return ResponseEntity.ok(razaService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Raza> buscarRazaPorId(@PathVariable Long id) {
        Optional<Raza> raza = razaService.searchForId(id);
        if (raza.isPresent()) {
            return ResponseEntity.ok(raza.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarRaza(@PathVariable Long id, @RequestBody RazaDTO razaDTO) {
        try {
            razaService.updateRaza(id, razaDTO);
            return ResponseEntity.ok("{\"message\": \"Raza modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarRaza(@PathVariable Long id) {
        Optional<Raza> razaOptional = razaService.searchForId(id);
        if (razaOptional.isPresent()) {
            razaService.deleteRaza(id);
            return ResponseEntity.ok("{\"message\": \"raza eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"raza no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}