package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.entidades.TallaDTO;
import com.grupo8.petshop.entity.Talla;
import com.grupo8.petshop.service.ITallaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/talla")
public class TallaController {
    private final ITallaService tallaService;

    public TallaController(ITallaService tallaService) {
        this.tallaService = tallaService;
    }

    @PostMapping
    public ResponseEntity<Talla> crearTalla(@RequestBody TallaDTO tallaDTO) {
        Talla tallaARetornar = tallaService.createTalla(tallaDTO);
        if (tallaARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(tallaARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<Talla>> traerTodos() {
        return ResponseEntity.ok(tallaService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Talla> buscarTallaPorId(@PathVariable Long id) {
        Optional<Talla> talla = tallaService.searchForId(id);
        if (talla.isPresent()) {
            return ResponseEntity.ok(talla.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarTalla(@PathVariable Long id, @RequestBody TallaDTO tallaDTO) {
        try {
            tallaService.updateTalla(id, tallaDTO);
            return ResponseEntity.ok("{\"message\": \"Talla modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarTalla(@PathVariable Long id) {
        Optional<Talla> tallaOptional = tallaService.searchForId(id);
        if (tallaOptional.isPresent()) {
            tallaService.deleteTalla(id);
            return ResponseEntity.ok("{\"message\": \"talla eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"talla no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}
