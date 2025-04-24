package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.entidades.EtiquetaDTO;
import com.grupo8.petshop.entity.Etiqueta;
import com.grupo8.petshop.service.IEtiquetaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/etiqueta")
public class EtiquetaController {
    private final IEtiquetaService etiquetaService;

    public EtiquetaController(IEtiquetaService etiquetaService) {
        this.etiquetaService = etiquetaService;
    }

    @PostMapping
    public ResponseEntity<Etiqueta> crearEtiqueta(@RequestBody EtiquetaDTO etiquetaDTO) {
        Etiqueta etiquetaARetornar = etiquetaService.createEtiqueta(etiquetaDTO);
        if (etiquetaARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(etiquetaARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<Etiqueta>> traerTodos() {
        return ResponseEntity.ok(etiquetaService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Etiqueta> buscarEtiquetaPorId(@PathVariable Long id) {
        Optional<Etiqueta> etiqueta = etiquetaService.searchForId(id);
        if (etiqueta.isPresent()) {
            return ResponseEntity.ok(etiqueta.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarEtiqueta(@PathVariable Long id, @RequestBody EtiquetaDTO etiquetaDTO) {
        try {
            etiquetaService.updateEtiqueta(id, etiquetaDTO);
            return ResponseEntity.ok("{\"message\": \"Etiqueta modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarEtiqueta(@PathVariable Long id) {
        Optional<Etiqueta> etiquetaOptional = etiquetaService.searchForId(id);
        if (etiquetaOptional.isPresent()) {
            etiquetaService.deleteEtiqueta(id);
            return ResponseEntity.ok("{\"message\": \"etiqueta eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"etiqueta no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}