package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.entidades.LoteDTO;
import com.grupo8.petshop.entity.Lote;
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
    public ResponseEntity<Lote> crearLote(@RequestBody LoteDTO loteDTO) {
        Lote loteARetornar = loteService.createLote(loteDTO);
        if (loteARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(loteARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<Lote>> traerTodos() {
        return ResponseEntity.ok(loteService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lote> buscarLotePorId(@PathVariable Long id) {
        Optional<Lote> lote = loteService.searchForId(id);
        if (lote.isPresent()) {
            return ResponseEntity.ok(lote.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/variante/{varianteId}")
    public ResponseEntity<List<Lote>> getLotesByVariante(@PathVariable Long varianteId) {
        List<Lote> lotes = loteService.searchByVariante(varianteId);
        return ResponseEntity.ok(lotes);
    }

    @GetMapping("/compra/{compraId}")
    public ResponseEntity<List<Lote>> getLotesByCompra(@PathVariable Long compraId) {
        List<Lote> lotes = loteService.searchByCompra(compraId);
        return ResponseEntity.ok(lotes);
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
        Optional<Lote> loteOptional = loteService.searchForId(id);
        if (loteOptional.isPresent()) {
            loteService.deleteLote(id);
            return ResponseEntity.ok("{\"message\": \"lote eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"lote no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}
