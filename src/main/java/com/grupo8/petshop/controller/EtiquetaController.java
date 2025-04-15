package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.EtiquetaDTO;
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
    public ResponseEntity<EtiquetaDTO> crearEtiqueta(@RequestBody EtiquetaDTO etiquetaDTO) {
        EtiquetaDTO etiquetaARetornar = etiquetaService.createEtiqueta(etiquetaDTO);
        if (etiquetaARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(etiquetaARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<EtiquetaDTO>> traerTodos() {
        return ResponseEntity.ok(etiquetaService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtiquetaDTO> buscarEtiquetaPorId(@PathVariable Long id) {
        Optional<EtiquetaDTO> etiqueta = etiquetaService.searchForId(id);
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
        Optional<EtiquetaDTO> etiquetaOptional = etiquetaService.searchForId(id);
        if (etiquetaOptional.isPresent()) {
            etiquetaService.deleteEtiqueta(id);
            return ResponseEntity.ok("{\"message\": \"etiqueta eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"etiqueta no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}