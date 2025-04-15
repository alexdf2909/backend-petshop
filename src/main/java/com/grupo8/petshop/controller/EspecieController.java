package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.EspecieDTO;
import com.grupo8.petshop.service.IEspecieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/especie")
public class EspecieController {
    private final IEspecieService especieService;

    public EspecieController(IEspecieService especieService) {
        this.especieService = especieService;
    }

    @PostMapping
    public ResponseEntity<EspecieDTO> crearEspecie(@RequestBody EspecieDTO especieDTO) {
        EspecieDTO especieARetornar = especieService.createEspecie(especieDTO);
        if (especieARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(especieARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<EspecieDTO>> traerTodos() {
        return ResponseEntity.ok(especieService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecieDTO> buscarEspeciePorId(@PathVariable Long id) {
        Optional<EspecieDTO> especie = especieService.searchForId(id);
        if (especie.isPresent()) {
            return ResponseEntity.ok(especie.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarEspecie(@PathVariable Long id, @RequestBody EspecieDTO especieDTO) {
        try {
            especieService.updateEspecie(id, especieDTO);
            return ResponseEntity.ok("{\"message\": \"Especie modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarEspecie(@PathVariable Long id) {
        Optional<EspecieDTO> especieOptional = especieService.searchForId(id);
        if (especieOptional.isPresent()) {
            especieService.deleteEspecie(id);
            return ResponseEntity.ok("{\"message\": \"especie eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"especie no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}