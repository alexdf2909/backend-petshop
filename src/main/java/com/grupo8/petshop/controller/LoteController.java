package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.LoteDTO;
import com.grupo8.petshop.service.ILoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lote")
public class LoteController {
    private final ILoteService loteService;

    public LoteController(ILoteService loteService) {
        this.loteService = loteService;
    }

    @PostMapping
    public ResponseEntity<LoteDTO> crearLote(@RequestBody LoteDTO loteDTO) {
        LoteDTO loteARetornar = loteService.createLote(loteDTO);
        if (loteARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(loteARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<LoteDTO>> traerTodos() {
        return ResponseEntity.ok(loteService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteDTO> buscarLotePorId(@PathVariable Long id) {
        Optional<LoteDTO> lote = loteService.searchForId(id);
        if (lote.isPresent()) {
            return ResponseEntity.ok(lote.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarLote(@PathVariable Long id, @RequestBody LoteDTO loteDTO) {
        try {
            loteService.updateLote(id, loteDTO);
            return ResponseEntity.ok("{\"message\": \"Lote modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarLote(@PathVariable Long id) {
        Optional<LoteDTO> loteOptional = loteService.searchForId(id);
        if (loteOptional.isPresent()) {
            loteService.deleteLote(id);
            return ResponseEntity.ok("{\"message\": \"lote eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"lote no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}
